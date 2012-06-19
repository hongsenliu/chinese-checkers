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
	
	public static Point oposite(Point p) {
		return new Point (sizeI-1-p.i, sizeJ-1-p.j );
	}
	
	public boolean[][] ji = new boolean [sizeJ][sizeI];
	
	public Board() {
	}
	public Board(boolean[][]tab) {
		ji = tab;
	}
	
	
	public boolean is(int i, int j) {
		return ji[j][i];
	}
	
	public boolean is(Point p) {
		return ji[p.j][p.i];
	}

	public void set(Point p) {
		ji[p.j][p.i] = true;
	}
	public void set(int i, int j) {
		ji[j][i] = true;
	}
	public void reset(Point p) {
		ji[p.j][p.i] = false;
	}
	
	public boolean  go(Point p, int d) {
		if (!p.go(d)) return false;
		return !is(p);
	}
	
	public boolean jump(Point p, int d) {
		if (!p.go(d)) return false;
		if (!is(p)) return false;		
		return go(p,d);
	}

	public boolean valid(Point a, Point z) {
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
		// Checks no other balls in the way (but the one in the middle of course)
		for (int k=1; k<l/2; k++) {
			if (dir==2 || dir==5) {
				if (dir==2 && (is(a.i+k,a.j) || is(z.i-k,z.j))) return false;
				if (dir==5 && (is(a.i-k,a.j) || is(z.i+k,z.j))) return false;
			} else {
				if (is(a,z,k,l) ) return false;
				if (is(z,a,k,l) ) return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true if point at distance @param d is a ball. Provided points @param a and @param z. Assuming they are in same line.
	 * separated by a even length @param l 
	 */
	private boolean is(Point a, Point z, int d, int l) {
		if (z.j<a.j) return is(z,a,l-d, l);
		if (a.isEven()) return is(a.i+d/2, a.j+d);
		else return is(a.i+(d+1)/2, a.j+d);
	}

	/**
	 * @return true if move is valid as topography and against present position of balls.
	 */
	public boolean valid(Move move) {
		List<Point> points = move.points;
		if (Move.isHop(points.get(0), points.get(1))) { 
			// move is a mono-step Go,
			if (points.size()>2) return false;
			// we need to check that target is free
			return !is(points.get(1));
		}
		// below only multi-step. Each step is a jump.
		for (int i=0; i<points.size()-1; i++) {
			Point a = points.get(i);
			Point z = points.get(i+1);
			if (Move.isHop(a,z)) return false;
			if ( !valid(a, z)) return false;
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
}