package org.scoutant.cc.model;

import java.util.Comparator;

/**
 * Compare 2 moves against their length, according to player direction.
 * So that a Collections.sort(., comparator) puts at first the longest moves.  
 */
public class MoveComparator implements Comparator<Move> {

	private int player;
	private Object game;

	public MoveComparator(Game game, int player) {
		this.game = game;
		this.player = player;
	}
	
	/**
	 * Core of the game, the AI evaluation function: <p>
	 * @return a scalar representing the difference of AI evaluation function for @param a and @param b.
	 * <p>According to (in following order) : <ul> 
	 * <li>player direction, 
	 * <li>distance to target point
	 * <li>distance to axis as second level
	 * <li>position of move origin peg in the game : giving preference to furthers pegs (so as not to be trapped with no more peg to jump over, in endgame)
	 * <li>position of move end point : preference to actually enter a peg in one's triangle. 
	 */
	@Override
	public int compare(Move a, Move b) {
		int targetDiff = b.length(player) - a.length(player);
		int axisDiff = b.axisLengh(player) - a.axisLengh(player);
		int originDiff = Board.length(player, b.point(0)) - Board.length(player, a.point(0));
		int intoTriangleDiff = intoTriangle(player, b) - intoTriangle(player, a); 
		return 2*Board.sizeJ*targetDiff - axisDiff - originDiff/4 + intoTriangleDiff;
	}

	private static int intoTriangle(int player,final Move m) {
		if (m==null || m.last()== null ) return 0;
		return (Board.length(player, m.last()) < 4 ? 1 : 0);
	}

}