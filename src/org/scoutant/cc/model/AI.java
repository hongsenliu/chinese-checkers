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
	
	public AI(Game game) {
		this.game = game;
		board = game.board;
		track = new Board();
	}
	public List<Move> thinkUpToNMoves() {
		List<Move> moves = new ArrayList<Move>();
		// TODO consider other player?
		Player player = game.player(0);
		for (Peg peg : player.pegs()) {
			Log.d(tag, "----------------------------------------------------------------------------------");
			Log.d(tag, "considering peg : " + peg);
			for (int dir:dirs ) {
				// consider jump in corresponding direction...
				Point p = board.jump(peg.point, dir);
				Log.d(tag, "dir : " + dir +", jump to : " + p);
			}
		}
		// consider hops only at least
		return moves;
	}
}
