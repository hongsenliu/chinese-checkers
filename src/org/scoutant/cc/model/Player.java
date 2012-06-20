package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

	/** Each player has a set of ten balls */ 
	private List<Peg> pegs = new ArrayList<Peg>();
	
	// TODO Or ensure sort in done only once when a move is done...
	public List<Peg> pegs() {
		Collections.sort(pegs);
		return pegs;
	}
	
	public Point goal;
	public int color;
	
	public Player(Point goal) {
		this.goal = goal;
	}
	public Player(int i, int j) {
		this(new Point(i,j));
	}
	public Player(int i, int j, int color) {
		this(i,j);
		this.color = color;
	}
	
	public Player add(Peg peg) {
		pegs.add(peg);
		peg.color = this.color;
		return this;
	}
	public Peg peg(int index) {
		return pegs.get(index);
	}
	
	public Player add(int i, int j) {
		return add( new Peg(i,j));
	}

	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Peg p : pegs) {
			ji[p.point.j][p.point.i] = true;
		}
		String msg = "";
		msg += "----------------player--------------------\n";
		for (int j=jmin; j<=jmax; j++) {
			if (j%2 == 1) msg += "  ";
			for (int i=0; i<Board.sizeI; i++) {
				msg += ji[j][i] ? " X " : "   "; 
			}
			msg += "\n";
		}
		msg += "------------------------------------------\n";
		return msg;
	}
	
}
