package org.scoutant.cc.model;

import java.util.ArrayList;
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
	public List<Move> thinkUpToNMoves() {
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
		return moves;
	}
	
	private void visite(Move move) {
		for (int dir:dirs ) {
			visite(move, dir);
		}
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
		moves.add(found);
		Log.d(tag, "move ! " + found);
		Log.d(tag, "+++++++++++++++++++++++++++++++++++++++++++");
		visite( found.clone());
		Log.d(tag, "-------------------------------------------");
	}
//	private void visite(Peg peg, int dir) {
//		Point p = board.jump(peg.point, dir);
////		Log.d(tag, "dir : " + dir +", jump to : " + p);
//		if (p==null) return;
//		if (track.is(p)) {
//			Log.d(tag, "allready visited point " + p);
//			return;
//		}
//		track.set(p);
//		Move move = new Move(peg.point);
//		move.add(p);
//		moves.add(move);
//	}
}
