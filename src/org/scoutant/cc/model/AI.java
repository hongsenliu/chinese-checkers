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
//		track = new Board();
		for (int i=0; i<6; i++) {
			comparators[i] = new MoveComparator( new MoveEvaluator(i));
			endgameComparators[i] = new MoveComparator( new EndgameMoveEvaluator(i));
		}
	}

	
	public Move think(int color, int level) {
		thinkUpToNJumps(color, level);
		Log.d(tag, "# of jumps : " + moves.size());
		if (moves.size()<=4) {
			// let's consider hops too
			thinkHops(color, level);
			Log.d(tag, "# of moves including hops : " + moves.size());
//			Collections.sort(moves, comparators[color]);
			Collections.sort(moves, endgameComparators[color]);
		}
		if (moves.size()==0) {
			// TODO endgame
			Log.d(tag, "no more moves... for player : " + color);
			return null;
		}
		// TODO random move among the 10 best ones...
		Move move = moves.get(0);
		return move ;
	}

	// TODO performance : consider a LOG constant to actually log only if ON!
	
	protected void thinkHops(int color, int level) {
		Player player = game.player(color);
		for (Peg peg : player.pegs()) {
			Log.d(tag, "**** hops ?");
			// consider only positive hops
			for (int i=0; i<2; i++) {
				int dir = dirs[color][i];
				Point p = board.hop(peg.point, dir);
				if (p!=null && !board.is(p)) {
					// target is a hole not occupied by a peg
					Move move = new Move( peg.point);
					move.add(p);
					moves.add( move);
					Log.d(tag, "can hop : " + move);
				}
			}
		}
	}
	
	/**
	 * @return the list of moves for given play. Considering only jumps.
	 */
	protected void thinkUpToNJumps(int color, int level) {
		// TODO level
		moves.clear();
		Player player = game.player(color);
		for (Peg peg : player.pegs()) {
			Log.d(tag, "*********************************************************************************");
			Log.d(tag, "peg : " + peg );
			Log.d(tag, "*********************************************************************************");
			Move move = new Move(peg.point);
			track = new Board();
			visite( color, move);
		}
		Collections.sort(moves, comparators[color]);
		Log.d(tag, "# of jumps : " + moves.size());
	}
	
	private void visite(int color, Move move) {
		for (int dir:dirs[color] ) {
//			Log.d(tag, "** dir : " + dir);
			visite(color, move, dir);
		}
	}
	
	private void visite(int color, Move move, int dir) {
		Point p = board.jump(move.last(), dir);
//		Log.d(tag, "dir : " + dir +", jump to : " + p);
		if (p==null) return;
		Point middle = Move.middle(p, move.last());
		if (move.point(0).equals(middle)) {
			Log.d(tag, "excluding jump over origin peg.");
			return;
		}
		if (track.is(p)) {
			Log.d(tag, "already visited point " + p);
			if (color == 5 && p.i==2 && p.j==7) Log.d(tag, track.toString(7, 10));
			return;
		}
		track.set(p);
		Log.d(tag, "setting track : " + p);
		Move found = move.clone();
		found.add(p);
		Log.d(tag, "move ? [ " + found.length(color) + " ] "+ found);
		// TODO many if considering zero length move even in middle game?
		if (found.length( color)>=0) {
			Log.d(tag, "move ! [ " + found.length(color) + " ] "+ found);
			moves.add(found);
		} 
//		Log.d(tag, "+++++++++++++++++++++++++++++++++++++++++++");
		visite( color, found.clone());
//		Log.d(tag, "-------------------------------------------");
	}

	/** for test purpose */
	protected List<Move> moves() { return moves; }
}