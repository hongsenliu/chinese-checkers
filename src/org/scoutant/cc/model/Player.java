package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

	/** Each player has a set of ten balls */ 
	public List<Piece> pieces = new ArrayList<Piece>();
	
	public Point goal;
	
	public Player(Point goal) {
		this.goal = goal;
	}
	public Player(int i, int j) {
		this(new Point(i,j));
	}
	
	public Player add(Piece piece) {
		pieces.add(piece);
		return this;
	}
	public Piece piece(int index) {
		return pieces.get(index);
	}
	
	public Player add(int i, int j) {
		return add( new Piece(i,j));
	}

	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Piece p : pieces) {
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
