package org.scoutant.cc.model;

import java.util.Comparator;

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
	
	@Override
	public int compare(Move a, Move b) {
		return b.lenght(player) - a.lenght(player);
	}

}
