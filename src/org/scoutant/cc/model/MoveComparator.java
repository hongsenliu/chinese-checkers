package org.scoutant.cc.model;

import java.util.Comparator;

/**
 * Compare 2 moves against their length, according to player direction.
 * So that a Collections.sort(., comparator) puts at first the longest moves.  
 */
public class MoveComparator implements Comparator<Move> {

	public static final MoveComparator[] comparators = {
		new MoveComparator(0),
		new MoveComparator(1),
		new MoveComparator(2),
		new MoveComparator(3),
		new MoveComparator(4),
		new MoveComparator(5)
	};
	
	private int player;

	public MoveComparator(int player) {
		this.player = player;
	}
	
	/**
	 * @return a number representing the difference of rows of @param a and @param b. According to player direction.
	 * As a second level criteria: move ending closest to board axis is preferred... 
	 */
	@Override
	public int compare(Move a, Move b) {
		int rowDiff = b.length(player) - a.length(player);
		int axisDiff = b.axisLengh(player) - a.axisLengh(player);
		return 2*Board.sizeJ*rowDiff - axisDiff;
	}

}
