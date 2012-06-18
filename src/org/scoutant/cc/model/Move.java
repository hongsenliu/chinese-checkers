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

	/** @return true if move is a Go to a neighbor point. Do not test is neighbor actually is a hole. */
	public static boolean isGo(Point a, Point z) {
		// TODO consider a==z also ?
		int di = Math.abs(z.i-a.i);
		int dj = Math.abs(z.j-a.j);
		if (di>2 || dj>2) return false;
		// (5,2) and (6,3) are not neighbors. Happens when point with biggest i has an odd j.
		int j = (a.i>z.i? a.j : z.j);
		if (di==1 && j%2==1) return false;
		return true;
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
	
	/**
	 * Points are supposed not to be equals. 
	 * @return -1 if points @param a and @param z are not in same line.
	 * @return direction 0, 1, 2, 3, 4 or 5 if points @param a and @param z are not in same line.
	 *<p>  0  1
	 *<p> 5    2
	 *<p>  4  3
	 */
	public final static int  direction(Point a, Point z) {
		int di = z.i-a.i;
		int dj = z.j-a.j;
		if (dj==0)return (di > 0 ? 2 : 5); 
		if (dj>0) { 
			int dir = direction (z,a);
			if (dir==-1) return -1;
			return dir +3;
		}
		// below dj<0 and only 0 or 1 is possible. Or not in line : -1
		int Di = Math.abs(di);
		int Dj = Math.abs(dj);
		// dj has to be more or less twice
		int DI = Dj/2;
		if (Dj%2==0) {
			if ( DI!=Di) return -1;
			return (di<0? 0 : 1);
		} else {
			if (a.isEven()) {
				if (di==DI) return 1;
				if (di==-DI-1) return 0;
			} else {
				if (di==DI+1) return 1;
				if (di==-DI) return 0;
			}
		}
		return -1;
	}
}


// neighbor or nearly
//if (Di<=1 && Dj==1) {
//	if (a.isOdd()) return (di==0 ? 0 : 1);
//	else {
//		if(di==1) return -1;
//		return (di==0 ? 1 : 0);
//	}
//}
// else dj has to be more or less twice
