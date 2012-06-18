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
	public int nbPieces;
	public int score;
	public boolean over=false;
	
	public Board() {
//		assert( Board.hole != null);
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
		// TODO checks no other balls in the way
		return true;
	}

	// TODO if a move begins with a no-jump, it has to be single step.
	// TODO move with jumps cannot have any single step...
	public boolean valid(Move move) {
		Point a = move.points.get(0);
		// if first step is a Go, move must be single step
		for (Point z : move.points) {
			if (!valid(a, z)) return false;
			a = z;
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