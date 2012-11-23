package org.scoutant.cc.model;

/**
 * Core of the game, the AI evaluation function: <p>
 * @return a scalar representing, in following order : <ul> 
 * <li>player direction, 
 * <li>distance to target point
 * <li>distance to axis as second level
 * <li>position of move origin peg in the game : giving preference to furthers pegs (so as not to be trapped with no more peg to jump over, in endgame)
 * <li>position of move end point : preference to actually enter a peg in one's triangle. 
 */
public class MoveEvaluator implements Evaluator<Move> {
	
	private int player;

	public MoveEvaluator(int player) {
		this.player = player;
	}
	
	// TODO add criteria including # of jumps : idea is to enforce jumping on same row so as to let free target hole for next peg move along on same track...
	@Override
	public int evaluate (Move m) {
		int target = m.length(player);
		int axis = m.axisLengh(player);
		int origin = Board.length(player, m.point(0));
		return 2*Board.sizeJ*target - axis + origin/2 + Board.sizeJ*leavingTriangle(player, m);
	}

	private static int leavingTriangle(int player,final Move m) {
//		if (m==null || m.point(0)==null || m.last()== null ) return 0;
		// TODO remove assert.
		if (m==null || m.point(0)==null || m.last()== null ) throw new IllegalAccessError("Move shall have at least 2 points!");
		return (Board.length(player, m.point(0))>12 && Board.length(player, m.last()) <= 12 ? 1 : 0);
	}

}
