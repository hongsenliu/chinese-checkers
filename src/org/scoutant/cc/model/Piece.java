package org.scoutant.cc.model;

public class Piece {
	
	public Point point;
	public int color;

	public Piece( Point p) {
		this.point = p;
	}
	public Piece(int i, int j) {
		this( new Point(i,j));
	}
	
	public String toString(){
		return ""+point + ", color :" + color;
	}

}
