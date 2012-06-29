package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO switch to platform Log when tests done...
//import android.util.Log;

public class AI {
	private static String tag = "ai";
	// Consider as first directions pointing to opposite triangle.
	public static final int[] dirs = {0, 1, 5, 2, 4, 3};  

	private Game game;
	private Board board;
	private Board track;
	private List<Move> moves = new ArrayList<Move>();;
	public AI(Game game) {
		this.game = game;
		board = game.board;
		track = new Board();
	}

	// TODO consider hops only at least
	/**
	 * @return the list of moves for given play. Considering only jumps.
	 */
	protected List<Move> thinkUpToNMoves() {
		track = new Board();
		moves.clear();
		// TODO consider other player?
		Player player = game.player(0);
		for (Peg peg : player.pegs()) {
			Log.d(tag, "*********************************************************************************");
			Log.d(tag, "peg : " + peg );
			Log.d(tag, "*********************************************************************************");
			Move move = new Move(peg.point);
			visite( move);
		}
		Collections.sort(moves);
		return moves;
	}
	
	private void visite(Move move) {
		for (int dir:dirs ) {
			visite(move, dir);
		}
	}
	
	public Move think(int color, int level) {
		// TODO color
		// TODO level
		List<Move> moves = thinkUpToNMoves();
		
		if (moves.size()==0) {
			// TODO endgame
		}
		// TODO random move among the 10 best ones...
		Move move = moves.get(0);
		return move ;
	}

	
	private void visite(Move move, int dir) {
		Point p = board.jump(move.last(), dir);
//		Log.d(tag, "dir : " + dir +", jump to : " + p);
		if (p==null) return;
		if (track.is(p)) {
			Log.d(tag, "allready visited point " + p);
			return;
		}
		track.set(p);
		Move found = move.clone();
		found.add(p);
		// TODO player 0?
		if (found.lenght( 0)>0) {
			Log.d(tag, "move ! " + found);
			moves.add(found);
		} 
		Log.d(tag, "+++++++++++++++++++++++++++++++++++++++++++");
		visite( found.clone());
		Log.d(tag, "-------------------------------------------");
	}
}
