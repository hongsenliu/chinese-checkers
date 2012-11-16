package org.scoutant.cc.model;

import java.util.Comparator;

/**
 * Compare 2 moves against their length, according to player direction.
 * So that a Collections.sort(., comparator) puts at first the longest moves.  
 */
public class MoveComparator implements Comparator<Move> {

	private Evaluator<Move> evaluator;
	public MoveComparator(Evaluator<Move> evaluator) {
		this.evaluator = evaluator;
	}
	
	@Override
	public int compare(Move a, Move b) {
		return (evaluator.evaluate(b) - evaluator.evaluate(a));
	}
}
// TODO clean
//int targetDiff = b.length(player) - a.length(player);
//int axisDiff = b.axisLengh(player) - a.axisLengh(player);
//int originDiff = Board.length(player, b.point(0)) - Board.length(player, a.point(0));
//int intoTriangleDiff = intoTriangle(player, b) - intoTriangle(player, a); 
//return 2*Board.sizeJ*targetDiff - axisDiff - originDiff/4 + intoTriangleDiff;
//}
