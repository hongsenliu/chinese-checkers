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
	private int player;
	private List<Peg> pegs;
	private Board track;
	private List<Move> moves = new ArrayList<Move>();
	private Comparator<Move> comparator; 
	private Comparator<Move> endgameComparator;
	
	public AI(Game game, int player) {
		this.game = game;
		this.player = player;
		board = game.board;
		comparator = new MoveComparator( new MoveEvaluator(player));
		endgameComparator = new MoveComparator( new EndgameMoveEvaluator(player));
	}
	
	public Move think() {
		// TODO exclude peg that has reached target!
		pegs = game.player(player).pegs();
		moves.clear();

		thinkJumps();
		Log.d(tag, "# of jumps : " + moves.size());
		if (moves.size()<=4) {
			thinkHops();
			Collections.sort(moves, endgameComparator);
			Log.d(tag, "# of moves including hops : " + moves.size());
		}
		log();
		if (moves.size()<=4) { // trapped in the 9-peg endgame issue?
			consider9PegEndgamePosition();
		}
		
		if (moves.size()==0) return null; // game over
		
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
	private void consider9PegEndgamePosition() {
		for( int i = 1 ; i<10; i++) {
			Peg peg = pegs.get(i);
			if (!Board.intoTriangle(player, peg.point)) return;
		}
		// the 9 pegs closest to goal are into triangle. 
		Peg peg = pegs.get(0);
		if (Board.length(player, peg.point)!= 4) return; // if length is 3, game actually is over...
		if (! peg.point.equals(Board.pivots[player]) ) return; // if not pivot, AI naturally find move toward pivot.
		Point target = null;
		for (Point point : Board.thirdRow(player)) { // which is the hole not occupied by none of the 9 pegs?
			if ( !board.is(point)) target = point;
		}
		if (target==null) return; // surely occupied by a peg of another player!
		// correct option is to move peg towards target.
		int rigthPlayer = (player+1)%6;
		Move move = new Move(peg.point);
		Log.d(tag, "Considering 9-peg endgame issue! distance of free hole to rigth-player goal : " + Board.length(rigthPlayer, target) +", " + target);
		move.add( Board.length(rigthPlayer, target)>6 ? Board.escapes[player][0] : Board.escapes[player][1]);
		moves.clear();
		moves.add(move);
	}

	protected void thinkHops() {
		for (Peg peg : pegs) {
			for (int i=0; i<4; i++) {
				int dir = dirs[player][i];
				Point p = board.hop(peg.point, dir);
				if (p!=null && !board.is(p)) {
					// target is a hole not occupied by a peg
					Move move = new Move( peg.point);
					move.add(p);
					moves.add( move);
				}
			}
		}
	}
	
	/**
	 * @return the list of moves for given play. Considering only jumps.
	 */
	protected void thinkJumps() {
		for (Peg peg : pegs) {
			Move move = new Move(peg.point);
			track = new Board();
			visite( move);
		}
		Collections.sort(moves, comparator);
	}
	
	private void visite(Move move) {
		for (int dir:dirs[player] ) {
			visite(move, dir);
		}
	}
	
	private void visite(Move move, int dir) {
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
		if (found.length( player)>=0) {
			moves.add(found);
		} 
		visite( found.clone());
	}

	/** for test purpose */
	protected List<Move> moves() { return moves; }
	
	private void log(Move move) {
		Log.d(tag, "move ! [ " + move.length(player) + " ] < " + new MoveEvaluator(player).evaluate(move) + ", "  + new EndgameMoveEvaluator(player).evaluate(move) + "> " + move);
	}
	private void log() {
		log( moves);
	}
	
	private void log(List<Move> moves) {
		Log.d(tag, "# of moves : " + moves.size());		
		for (Move move :moves) {
			log(move);
		}
	}
	
}