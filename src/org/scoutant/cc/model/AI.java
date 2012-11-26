/*
* Copyright (C) 2012- stephane coutant
*
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Players in this order :
 *<p>    3 
 *<p> 2     4
 *<p> 1     5
 *<p>    0
 *
 *<p>Directions in the board : 
 *<p>  0  1
 *<p> 5    2
 *<p>  4  3
 *
 */
public class AI {
	private static String tag = "ai";
	/** Consider as first directions pointing to opposite triangle. */
	public static final int[][] dirs = {
		{0, 1, 5, 2, 4, 3}, // optimal directions for player : 0  
		{1, 2, 0, 3, 5, 4}, // 1  
		{2, 3, 1, 4, 0, 5}, // 2 
		{3, 4, 2, 5, 1, 0}, // 3 
		{4, 5, 3, 0, 2, 1}, // 4 
		{5, 0, 4, 1, 3, 2}, // 5 
	};

	private Game game;
	private Board board;
	private Board track;
	private List<Move> moves = new ArrayList<Move>();
	private Comparator<Move>[] comparators = new MoveComparator[6]; 
	private Comparator<Move>[] endgameComparators = new MoveComparator[6]; 
	public AI(Game game) {
		this.game = game;
		board = game.board;
		for (int i=0; i<6; i++) {
			comparators[i] = new MoveComparator( new MoveEvaluator(i));
			endgameComparators[i] = new MoveComparator( new EndgameMoveEvaluator(i));
		}
	}

	
	public Move think(int color) {
		thinkJumps(color);
		Log.d(tag, "# of jumps : " + moves.size());
		if (moves.size()<=4) {
			// let's consider hops too
			thinkHops(color);
			Collections.sort(moves, endgameComparators[color]);
			Log.d(tag, "# of moves including hops : " + moves.size());
//			log(color);
		}
		log(color);
		if (moves.size()<=0) { // trapped in the 9-peg endgame issue?
			consider9PegEndgamePosition(color);
		}
		
		if (moves.size()==1) {
			// TODO check if game over!
		}
		
		if (moves.size()==0) {
			// TODO end of game
			Log.d(tag, "no more moves... for player : " + color);
			return null;
		}
		// TODO random move among the 10 best ones...
		Move move = moves.get(0);
		return move ;
	}

	// TODO performance : consider a LOG constant to actually log only if ON!
	
	/**
	 * <p> 9-pegs endgame issue :
	 * <li>. . X . .
	 * <li> . X X X
	 * <li>  X X X
	 * <li>   X X
	 * <li>    X
	 */
	private void consider9PegEndgamePosition(int color) {
		List<Peg> pegs = game.player(color).pegs();
		for( int i = 1 ; i<10; i++) {
			Peg peg = pegs.get(i);
			if (!Board.intoTriangle(color, peg.point)) return;
		}
		// the 9 pegs closest to goal are into triangle. 
		Peg peg = pegs.get(0);
		if (Board.length(color, peg.point)!= 4) return; // if length is 3, game actually is over...
		Point target = null;
		for (Point point : Board.thirdRow(color)) { // which is the hole not occupied by none of the 9 pegs?
			if ( !board.is(point)) target = point;
		}
		if (target==null) return; // surely occupied by a peg of another player!
		// correct option is to move peg towards target.
		if (! target.equals(Board.pivots[color]) ) return; // if not pivot, AI naturally find move toward pivot.
		int leftPlayer = (color+5)%6;
		Move move = new Move(peg.point);
		move.add( Board.length(leftPlayer, target)<6 ? Board.escapes[color][0] : Board.escapes[color][1]);
		moves.add(move);
	}


	protected void thinkHops(int color) {
		Player player = game.player(color);
		// TODO exclude peg that has reached target!
		for (Peg peg : player.pegs()) {
			Log.d(tag, "**** hops ?");
			// do not consider negative hops, would be necessary in a multi-turn AI...
			for (int i=0; i<4; i++) {
				int dir = dirs[color][i];
				Point p = board.hop(peg.point, dir);
				if (p!=null && !board.is(p)) {
					// target is a hole not occupied by a peg
					Move move = new Move( peg.point);
					move.add(p);
					moves.add( move);
//					log(color, move);
				}
			}
		}
	}
	
	/**
	 * @return the list of moves for given play. Considering only jumps.
	 */
	protected void thinkJumps(int color) {
		moves.clear();
		Player player = game.player(color);
		for (Peg peg : player.pegs()) {
//			Log.d(tag, "*********************************************************************************");
//			Log.d(tag, "peg : " + peg );
//			Log.d(tag, "*********************************************************************************");
			Move move = new Move(peg.point);
			track = new Board();
			visite( color, move);
		}
		Collections.sort(moves, comparators[color]);
//		log(color);
	}
	
	private void visite(int color, Move move) {
		for (int dir:dirs[color] ) {
			visite(color, move, dir);
		}
	}
	
	private static void log(int player, Move move) {
		Log.d(tag, "move ! [ " + move.length(player) + " ] < " + new MoveEvaluator(player).evaluate(move) + ", "  + new EndgameMoveEvaluator(player).evaluate(move) + "> " + move);
	}
	private void log(int player) {
		log(player, moves);
	}
	
	private static void log(int player, List<Move> moves) {
		Log.d(tag, "# of moves : " + moves.size());		
		for (Move move :moves) {
			log(player, move);
		}
	}
	
	
	private void visite(int color, Move move, int dir) {
		Point p = board.jump(move.last(), dir);
		if (p==null) return;
		Point middle = Move.middle(p, move.last());
		if (move.point(0).equals(middle)) {
			Log.d(tag, "excluding jump over origin peg.");
			return;
		}
		if (track.is(p)) {
			return;
		}
		track.set(p);
		Move found = move.clone();
		found.add(p);
		// TODO many if considering zero length move even in middle game?
		if (found.length( color)>=0) {
			moves.add(found);
//			log(color, found);
		} 
//		Log.d(tag, "+++++++++++++++++++++++++++++++++++++++++++");
		visite( color, found.clone());
//		Log.d(tag, "-------------------------------------------");
	}

	/** for test purpose */
	protected List<Move> moves() { return moves; }
}