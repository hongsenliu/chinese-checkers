package org.scoutant.cc.model;

/**
 * Core of the machine playing: the AI evaluation function: <p>
 * @return a scalar representing the move's scoring. Taking into account : <ul> 
 * <li>player direction, 
 * <li>distance to target point
 * <li>distance to axis as second level
 * <li>position of move origin peg in the game : giving preference to furthers pegs (so as not to be trapped with no more peg to jump over, in endgame)
 * <li>position of move end point : preference to actually enter a peg in one's triangle.
 * <li># of jumps : idea is to enforce jumping on same row so as to let free target hole for next peg move along on same track...
 * <li>bonus for playing the '0 peg', for it can easily be trapped later 
 * <li>malus if ending move in 3-party triangle (in addition to natural length criteria)
 * <li>bonus for exiting a 3-party triangle? 
 */
public class MoveEvaluator implements Evaluator<Move> {
	public static final Point[] origins = {
		new Point(6,16),
		new Point(0,12),
		new Point(0,4),
		new Point(6,0),
		new Point(12,4),
		new Point(12,12)
	};
	private int player;

	public MoveEvaluator(int player) {
		this.player = player;
	}
	
	@Override
	public int evaluate (Move m) {
		int target = m.length(player);
		int axis = m.axisLengh(player);
		int origin = Board.length(player, m.point(0));
		int is0Peg = origins[player].equals( m.point(0)) ? 1 : 0;
		return 2*Board.sizeJ*target - axis + origin/2 + Board.sizeJ*leavingTriangle(player, m) + m.points.size() + 2*Board.sizeJ*is0Peg;
	}

	private static int leavingTriangle(int player,final Move m) {
//		if (m==null || m.point(0)==null || m.last()== null ) return 0;
		// TODO remove assert.
		if (m==null || m.point(0)==null || m.last()== null ) throw new IllegalAccessError("Move shall have at least 2 points!");
		return (Board.length(player, m.point(0))>12 && Board.length(player, m.last()) <= 12 ? 1 : 0);
	}

}
