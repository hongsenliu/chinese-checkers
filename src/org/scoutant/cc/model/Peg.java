package org.scoutant.cc.model;

public class Peg implements Comparable<Peg> {
	
	public Point point;
	public int color;

	public Peg( Point p) {
		this.point = p;
	}
	private Peg(int i, int j) {
		this( new Point(i,j));
	}
	public Peg(int i, int j, int color) {
		this(i,j);
		this.color = color;
	}
	
	public String toString(){
		return ""+point + ", color :" + color;
	}
	/**
	 * So as to consider as first candidate the pegs closest to bottom
	 */
	@Override
	public int compareTo(Peg that) {
		return -(this.point.j - that.point.j);
	}

}
