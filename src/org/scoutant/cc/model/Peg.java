package org.scoutant.cc.model;

public class Peg {
	
	public Point point;
	public int color;

	public Peg( Point p) {
		this.point = p;
	}
	public Peg(int i, int j) {
		this( new Point(i,j));
	}
	
	public String toString(){
		return ""+point + ", color :" + color;
	}

}
