package org.scoutant.cc.model;


public class Point {
	
	public int i;
	public int j;
	
	public Point(int i, int j){
		this.i = i ;
		this.j = j;
	}
	public Point(Point p){
		this( p.i, p.j);
	}
	public Point(Piece piece){
		this(piece.point);
	}

	
	public String toString(){
		return String.format("(%d, %d)", i, j);
	}
	
	public boolean hole() {
		if (i<0 || i>=Board.sizeI || j<0 || j>=Board.sizeJ) return false;
		return Board.hole.is(this);		
	}

	/** go to neighbor hole in provided direction @param d 
	 *<p>   0  1
	 *<p> 5  *  2
	 *<p>   4  3
	 */
	public boolean go(int d) {
		if (d<0 || d> 6) return false;
		switch (d) {
		case 0: decrement() ; j--; break;
		case 1: increment() ; j--; break;
		case 2: i++			;    ; break;
		case 3: increment() ; j++; break;
		case 4: decrement() ; j++; break;
		case 5: i--			;    ; break;
		}
		return hole();
	}

	/** --> */ 
	public void increment() {
		if ( odd(j)) i++;
	}
	/** <-- */ 
	public void decrement() {
		if ( even(j)) i--;
	}
	
	private static boolean odd(int value) {
		return (value % 2 > 0);
	}
	private static boolean even(int value) {
		return (value % 2 == 0);
	}
	
	/** jump to hole in provided direction @param d. @throws NotAHoleException  */
//	public void jump(int d) throws NotAHoleException {
//		go(d);
//		go(d);
//	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		Point other = (Point) obj;
		return (i==other.i) && (j==other.j);
	}

	/** @return a represention of the piece, like this sample : 2:I3:0,-1:0,0:0,1 */
	public static String serialize(Point p) {
		return String.format( ":%d:%d", p.i, p.j);
	}
	
	
}
