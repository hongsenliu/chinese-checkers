package org.scoutant.cc.model;

/**
 * The AI evaluation function, suitable during end game. ie when the # of positive jumps is low. Considering both jump and hop.<p>
 * @return a scalar representing, in following order : <ul> 
 * <li>player direction, 
 * <li>distance to target point
 * <li>distance to axis as second level
 */
public class EndgameMoveEvaluator implements Evaluator<Move> {
	
	private int player;

	public EndgameMoveEvaluator(int player) {
		this.player = player;
	}
	@Override
	public int evaluate (Move m) {
		int target = m.length(player);
		int axis = m.axisLengh(player);
		return 2*Board.sizeJ*target - axis;
	}

}
