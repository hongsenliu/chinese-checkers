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
	
	/** valid if points actually are holes and if points are in same line. But does not checks for balls at all! */
	public static boolean valid (Point a, Point z) {
		if (a.equals(z)) return true;
		if ( !a.hole() || !z.hole()) return false;
		int dir = direction(a,z);
		if (dir==-1) return false; // not in same line
		int l = lenght(a, z, dir);
		if (l==1) return true; // it's a go to neighbor
		if (l%2==1) return false; // length cannot be odd!
		return true;
	}

	/** @return true if move is a Go to a neighbor point. Do not test is neighbor actually is a hole. 
	 * We suppose a not equal to z
	 */
	public static boolean isGo(Point a, Point z) {
		int di = Math.abs(z.i-a.i);
		int dj = Math.abs(z.j-a.j);
		if (di>=2 || dj>=2) return false;
		// (5,2) and (6,3) are not neighbors. Happens when point with biggest i has an odd j.
		int j = (a.i>z.i? a.j : z.j);
		if (di==1 && j%2==1) return false;
		return true;
	}
	
	/**
	 * @return middle of points @param a and @param z.
	 * <p>Precondition : Points are supposed to be in same line and a even distance; 
	 */
	public static Point middle (Point a, Point z) {
		int o = (a.isOdd()?1:0);
		return new Point( (a.i+z.i+o)/2, (a.j+z.j)/2);
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
	public final static int direction(Point a, Point z) {
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
	/** Points @param a and @param z are supposed to be in line with @param direction
	 * @return corresponding length
	 */
	public static int lenght(Point a, Point z, int direction) {
		if (a.equals(z)) return 0;
		int dir = direction(a, z);
		if (dir==-1) return -1;
		if (dir==2 || dir==5) return Math.abs(a.i-z.i);
		return Math.abs(z.j-a.j);
	}
	
}