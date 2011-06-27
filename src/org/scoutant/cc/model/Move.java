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

	public Move add(int i, int j) {
		return add( new Point(i,j));
	}
	public Move add(Point p) {
		points.add(p);
		return this;
	}
	
	/** valid is points actually are holes and if points are in same line. But does not checks for balls! */
	public static boolean valid (Point a, Point z) {
		a.hole();
		z.hole();
		int di = z.i-a.i;
		int dj = z.j-a.j;
		if (Math.abs(di)<=1 && Math.abs(dj)<=1 ) return true;
		if (Math.abs(di)==2 && dj==0 ) return true; // on j axis :
		if (Math.abs(di)==1 && Math.abs(dj)==2 ) return true; // on transversal axis :
		return false;
	}
	
	/** Supposed to be valid, @return length between provided points */
	public static int lenght(Point a, Point z) {
		int di = z.i-a.i;
		int dj = z.j-a.j;
		if (di==0 && dj==0) return 0;
		if (Math.abs(di)<=1 && Math.abs(dj)<=1 ) return 1;
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
		int i = (a.i+z.i)/2;
		return new Point(i,j);
	}
	
	
}
