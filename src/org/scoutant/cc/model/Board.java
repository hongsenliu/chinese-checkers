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
	static {
		init( true);
	}
	public static final String tag = "sc";
	/** With  @param regular to false, initialization to a mini-board game.*/ 
	public static void init(boolean regular) {
		radiusI = (regular ? 6 : 5);
		radiusJ = (regular ? 8 : 6);
		sizeI = 2*radiusI+1;
		sizeJ = 2*radiusJ+1;
		origin = new Point(sizeI/2, sizeJ/2);
		center = new Board( regular ? centreJI6 : centreJI5);
		hole = new Board( regular ? holeJI6 : holeJI5);
		}
	public static int radiusI;
	public static int radiusJ;
	public static int sizeI;
	public static int sizeJ;
	public static Point origin;
	public static Board center; 
	public static Board hole; 
	
	public static Point oposite(Point p) {
		return new Point (sizeI-1-p.i, sizeJ-1-p.j );
	}
	
	public boolean[][] ji = new boolean [sizeJ][sizeI];
	public int nbPieces;
	public int score;
	public boolean over=false;
	
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
		if (!Move.valid(a, z)) return false;
		int d = Move.lenght(a, z);
		if (d<= 1) return true;
		// below only jumps :
		Point middle = Move.middle(a, z);
		if (middle== null) return false;
		if(!is(middle)) return false;
		// TODO long jump, other holes shall be empty...
		return true;
	}

	public boolean valid(Move move) {
		Point a = move.points.get(0);
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

	private static final boolean[][] holeJI6 = { 
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
	
	private static final boolean[][] centreJI6 = { 
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

	private static final boolean[][] holeJI5 = { 
	{ false, false, false, false, false,  true, false, false, false, false, false}, // 0
		
		{ false, false, false, false,  true,  true, false, false, false, false, false}, // 1
		
	{ false, false, false, false,  true,  true,  true, false, false, false, false}, // 2
				
		{  true,  true,  true,  true,  true,  true,  true,  true,  true,  true, false}, // 3
	
	{ false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false}, // 4
		
		{ false,  true,  true,  true,  true,  true,  true,  true,  true, false, false}, // 5
	
	{ false, false,  true,  true,  true,  true,  true,  true,  true, false, false}, // 6 ------------------
		
		{ false,  true,  true,  true,  true,  true,  true,  true,  true, false, false}, // 7 (5)
			
	{ false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false}, // 8 (4)
		
		{  true,  true,  true,  true,  true,  true,  true,  true,  true,  true, false}, // 9 (3)
	
	{ false, false, false, false,  true,  true,  true, false, false, false, false}, // 10 (2)
				
		{ false, false, false, false,  true,  true, false, false, false, false, false}, // 11 (1)
	
	{ false, false, false, false, false,  true, false, false, false, false, false}, // 12 (0)
	};

	// sizeI=10, sizeJ=13
	private static final boolean[][] centreJI5 = { 
	{ false, false, false, false, false, false, false, false, false, false, false}, // 0
		
		{ false, false, false, false, false, false, false, false, false, false, false}, // 1
		
	{ false, false, false, false, false, false, false, false, false, false, false}, // 2
				
		{ false, false, false,  true,  true,  true,  true, false, false, false, false}, // 3
	
	{ false, false, false,  true,  true,  true,  true,  true, false, false, false}, // 4
		
		{ false, false,  true,  true,  true,  true,  true,  true,  true, false, false}, // 5
	
	{ false, false,  true,  true,  true,  true,  true,  true,  true, false, false}, // 6 ------------------

		{ false, false,  true,  true,  true,  true,  true,  true,  true, false, false}, // 7 (5)
	
	{ false, false, false,  true,  true,  true,  true,  true, false, false, false}, // 8(4)
	
		{ false, false, false,  true,  true,  true,  true, false, false, false, false}, // 9(3)
		
	{ false, false, false, false, false, false, false, false, false, false, false}, // 10(2)
			
		{ false, false, false, false, false, false, false, false, false, false, false}, // 11(1)

	{ false, false, false, false, false, false, false, false, false, false, false}, // 12(0)
	
	};
	
}
