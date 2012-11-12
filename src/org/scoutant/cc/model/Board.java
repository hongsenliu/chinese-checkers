/*
* Copyright (C) 2011- stephane coutant
*
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;


public class Board {
	public static final String tag = "model";
	private static final boolean[][] holeJI = { 
		{ false, false, false, false, false, false, true, false, false, false, false, false, false }, // 0

			{ false, false, false, false, false,  true, true, false, false, false, false, false, false }, // 1

		{ false, false, false, false, false,  true, true,  true, false, false, false, false, false }, // 2

			{ false, false, false, false,  true,  true, true,  true, false, false, false, false, false }, // 3
			
		{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true,  true }, // 4
		
			{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 5
			
		{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 6
		
			{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 7
			
		{ false, false,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 8 ----------------------
		
			{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 7
			
		{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 6
		
			{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 5
			
		{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true,  true }, // 4
		
			{ false, false, false, false,  true,  true, true,  true, false, false, false, false, false }, // 3
			
		{ false, false, false, false, false,  true, true,  true, false, false, false, false, false }, // 2
		
			{ false, false, false, false, false,  true, true, false, false, false, false, false, false }, // 1
			
		{ false, false, false, false, false, false, true, false, false, false, false, false, false }, // 0
		};

	private static final boolean[][] centreJI = { 
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 0
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 1
			
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 2
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 3
			
		{ false, false, false, false,  true,  true,  true,  true,  true, false, false, false, false }, // 4
		
			{ false, false, false,  true,  true,  true,  true,  true,  true, false, false, false, false }, // 5
			
		{ false, false, false,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 6
		
			{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 7
			
		{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 8 ----------------------
		
			{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 7
		
		{ false, false, false,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 6
		
			{ false, false, false,  true,  true,  true,  true,  true,  true, false, false, false, false }, // 5
		
		{ false, false, false, false,  true,  true,  true,  true,  true, false, false, false, false }, // 4
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 3
		
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 2
	
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 1
		
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 0
		};

	public static int radiusI = 6;
	public static int radiusJ = 8;
	public static int sizeI = 2*radiusI+1;
	public static int sizeJ = 2*radiusJ+1;
	public static Point origin = new Point(sizeI/2, sizeJ/2);
	public static Board center = new Board( centreJI ); 
	public static Board hole = new Board( holeJI ); 
	
	public static boolean hole(Point p) {
		if (p.i<0 || p.i>= sizeI || p.j<0 || p.j>=sizeJ) return false;
		return hole.is(p);
	}

	public static Point oposite(Point p) {
		return new Point (sizeI-1-p.i, sizeJ-1-p.j );
	}
	
	public boolean[][] ji = new boolean [sizeJ][sizeI];
	
	public Board() {
	}
	private Board(final boolean[][]tab) {
		ji = tab;
	}
	
	
	public boolean is(int i, int j) {
		return ji[j][i];
	}
	
	public boolean is(final Point p) {
		return ji[p.j][p.i];
	}

	public void set(final Point p) {
		ji[p.j][p.i] = true;
	}
	public void set(int i, int j) {
		ji[j][i] = true;
	}
	public void reset(Point p) {
		ji[p.j][p.i] = false;
	}
	public void reset(int i, int j) {
		ji[j][i] = false;
	}

	
	/** 
	 * Hop to neighbor hole in provided direction @param d 
	 *<p>   0  1
	 *<p> 5  *  2
	 *<p>   4  3
	 * @return the new Point identified by a hop in provided direction @param dir.
	 * Or null if hopping out of board.
	 * <p> Does not check if target happens to be filled with a ball  
	 */
	public final Point hop(Point p, int d) {
		if (p==null || d<0 || d> 6) return null;
		Point t = p.clone();
		switch (d) {
			case 0: t.decrement();	t.j--;	break;
			case 1: t.increment();	t.j--;	break;
			case 2: t.i++;	break;
			case 3: t.increment();	t.j++;	break;
			case 4: t.decrement();	t.j++;	break;
			case 5: t.i--;	break;
		}
		return hole(t) ? t : null;
	}
	
	/**
	 * If any @return the point corresponding to the first ball, starting from origin @param o in provided direction @param dir.
	 */
	public Point ball(Point o, int dir) {
		for (Point p : points(o,dir)) {
			if (is(p)) return p;
		}
		return null;		
	}

	/** 
	 * Short jump in provided direction @param d 
	 * @return the new Point or null if :
	 * <li>jumping out of board,
	 * <li>no ball to jump over
	 * <li>target is not free  
	 */
	public final Point jump(Point p, int d) {
		Point ball = ball(p,d);
		if (ball==null) return null;
		// let's checks all points till target actually are free and actually are hole!
		int length = Move.lenght(p, ball, d);
		// we are jumping out of board if the nb of points below does not reach the length to middle point
		List<Point> points = points(ball,d);
		if (points.size()<length) return null;
		if (points.size()==0) return null;
		Point t=null;
		for (int k=0; k<length; k++) {
//			Log.d(tag, "p : " + p +" d : " + d + ", k : " + k + ", ball : " + ball);
			t = points.get(k);
			if ( is(t)) return null;
		}
		return t;
	}

	/**
	 * @return true if points @param a and @param z are extremities of a valid jump : <ul>
	 * <li> z is a free hole
	 * <li> a and z are in same line
	 * <li> distance between a and z is even
	 * <li> middle point is a ball
	 * <li> but middle point cannot be the move's origin, which is @param origin 
	 * <li> all other intermediate holes are free   
	 */
	public boolean valid(Point a, Point z, Point origin) {
		if (a.equals(z)) return true;
		if(is(z)) return false;
		int dir = Move.direction(a, z);
		if (dir==-1) return false;
		int l = Move.lenght(a, z, dir);
		if (l<= 1) return true;
		// length must be even
		if (l%2!=0) return false;
		// below only true jumps, we just have to check : 1) a ball in the middle and 2) no other balls in the way.
		Point middle = Move.middle(a, z);
		if(!is(middle)) return false;
		if (middle.equals(origin)) {
			Log.d(tag, "cannot jump over one-self!");
			return false;
		}
		// Checks no other balls in the way (but the one in the middle of course)
		for (int k=1; k<l/2; k++) {
			if (dir==2 || dir==5) {
				if (dir==2 && (is(a.i+k,a.j) || is(z.i-k,z.j))) return false;
				if (dir==5 && (is(a.i-k,a.j) || is(z.i+k,z.j))) return false;
			} else {
				if (is(a,z,dir,k,l) ) return false;
				if (is(z,a,(dir+3)%6,k,l) ) return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true if point at distance @param dist is a ball. Provided points @param a and @param z. Assuming they are in same line
	 * with direction @param dir (possible values 0, 1, 3 or 4). Points are separated by a even length @param l.  
	 */
	protected boolean is(Point a, Point z, int dir, int dist, int l) {
		switch (dir) {
		case 3:
			if (a.isEven()) return is(a.i+dist/2, a.j+dist);
			else return is(a.i+(dist+1)/2, a.j+dist);
		case 4:
			if (a.isEven()) return is(a.i-(dist+1)/2, a.j+dist);
			else return is(a.i-(dist+1)/2, a.j+dist);
		case 0:
			if (a.isOdd()) return is(a.i-dist/2, a.j-dist);
			else return is(a.i-(dist+1)/2, a.j-dist);
		case 1:
			if (a.isEven()) return is(a.i+(dist+1)/2, a.j-dist);
			else return is(a.i+(dist+1)/2, a.j-dist);
		default:
			throw new IllegalArgumentException("Bad direction provided");
		}
	}

	/**
	 * @return true if move is valid as topography and against present position of balls.
	 */
	public boolean valid(Move move) {
		List<Point> points = move.points;
		if (Move.isHop(points.get(0), points.get(1))) { 
			// move is a hop,
			if (points.size()>2) return false;
			// we need to check that target is free
			return !is(points.get(1));
		}
		// below only multi-step move. Each step is a jump. May be mono jump.
		for (int i=0; i<points.size()-1; i++) {
			Point a = points.get(i);
			Point z = points.get(i+1);
			if (Move.isHop(a,z)) return false;
			if ( !valid(a, z, points.get(0))) return false;
		}
		return true;
	}
	
	public String toString() {
		return toString(0, sizeJ-1);
	}
	public String toString(int jmin, int jmax) {
		String msg = "";
		msg += "----------------board---------------------\n";
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
	
	
	/**
	 * @return the list of points of the board, starting from origin @param o in provided direction @param dir.
	 */
	public List<Point> points(Point o, int dir) {
		List<Point> points = new ArrayList<Point>();
		Point p = o;
		for (int k=1; k<sizeI; k++) {
			p = hop(p, dir);
			if (p==null) return points;
			points.add(p);
		}
		return points;
	}
}