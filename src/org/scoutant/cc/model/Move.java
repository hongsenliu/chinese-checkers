package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;

public class Move {
	
//	public Piece piece;
	public int score;
	public List<Point> points = new ArrayList<Point>();

	public Point point(int index) {
		return points.get(index);
	}

	public Point last() {
		if (points.isEmpty()) return null;
		return points.get(points.size()-1);
	}
	public Point penultima() {
		if (points.size()<2) return null;
		return points.get(points.size()-2);
	}
	
	public Move() {
	}
	
	public Move(Point p) {
		this();
		add(p);
	}
	public Move(Piece piece) {
		this( piece.point);
	}
	
	
	public Move add(int i, int j) {
		return add( new Point(i,j));
	}
	public Move add(Point p) {
		points.add(p);
		return this;
	}

	public String toString() {
		String str = "";
		for (Point p : points) {
			str += p + " ";
		}
		return str;
	}
	
	/** valid if points actually are holes and if points are in same line. But does not checks for balls! */
	public static boolean valid (Point a, Point z) {
		if ( !a.hole() || !z.hole()) return false;  
		int di = z.i-a.i;
		int dj = z.j-a.j;
		int j = (a.i>z.i? a.j : z.j);
		if (Math.abs(di)==1 && Math.abs(dj)==1 && j%2==1) return false;
		if (Math.abs(di)<=1 && Math.abs(dj)<=1 ) return true;
		if (Math.abs(di)==2 && dj==0 ) return true; // on j axis :
		if (Math.abs(di)==1 && Math.abs(dj)==2 ) return true; // on transversal axis :
		return false;
	}
	
	/** Supposed to be valid, @return length between provided points. This function is symetric. */
	public static int lenght(Point a, Point z) {
		int j = (a.i>z.i? a.j : z.j);
		int di = z.i-a.i;
		int dj = z.j-a.j;
		if (di==0 && dj==0) return 0;
//		if (Math.abs(di)<=1 && Math.abs(dj)<=1 ) return 1;
		if (Math.abs(di)<=1 && Math.abs(dj)<=1 ) return 1 + (Math.abs(di)==1 && j%2==1? 1 : 0);
		if (Math.abs(di)==2 && dj==0 ) return 2; // on j axis :
		if (Math.abs(di)==1 && Math.abs(dj)==2 ) return 2; // on transversal axis :
		// TODO long jump length!
		return -1;
	}

	public static Point middle (Point a, Point z) {
		if (!valid(a, z)) return null;
		int d = lenght(a,z);
		if (d!=2) return null; // TODO long jump!
		int j = (a.j+z.j)/2;
		int oI = (j%2==0 ? 1 : 0);
		// int i = (a.i+z.i+1)/2;
		int i = (a.i+z.i + oI)/2;
		return new Point(i,j);
	}

	public Move pop() {
		if (!points.isEmpty()) points.remove( points.size()-1);
		return this;
	}
	
}
