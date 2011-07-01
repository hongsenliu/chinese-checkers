package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	public List<Player> players = new ArrayList<Player>();
	public Board ball;
	public Player player(int index) {
		return players.get(index);
	}
	
	public Game() {
		this(true);
	}

	/**
	 * creating players in this order :
	 *<p>    0
	 *<p> 5     1
	 *<p> 4     2
	 *<p>    3 
	 */
	public Game(boolean regular) {
		// OK? TODO refactor to subclass and override constructor?
		Board.init(regular);
		ball = new Board();
		int color=0;
		if (regular) {
			// TODO color!
			players.add( new Player( 6, 0).add( 6,16).add( 5,15).add( 6,15).add( 5,14).add( 6,14).add( 7,14).add( 4,13).add( 5,13).add( 6,13).add( 7,13) ); // 0
			players.add( new Player(12, 4).add( 0,12).add( 1,12).add( 2,12).add( 3,12).add( 0,11).add( 1,11).add( 2,11).add( 1,10).add( 2,10).add( 1, 9) ); // 1
			players.add( new Player(12,12).add( 0, 4).add( 1, 4).add( 2, 4).add( 3, 4).add( 0, 5).add( 1, 5).add( 2, 5).add( 1, 6).add( 2, 6).add( 1, 7) ); // 2
			players.add( new Player( 6,16).add( 6, 0).add( 5, 1).add( 6, 1).add( 5, 2).add( 6, 2).add( 7, 2).add( 4, 3).add( 5, 3).add( 6, 3).add( 7, 3) ); // 3
			players.add( new Player( 0,12).add( 9, 4).add(10, 4).add(11, 4).add(12, 4).add( 9, 5).add(10, 5).add(11, 5).add(10, 6).add(11, 6).add(10, 7) ); // 4
			players.add( new Player( 0, 4).add( 9,12).add(10,12).add(11,12).add(12,12).add( 9,11).add(10,11).add(11,11).add(10,10).add(11,10).add(10, 9) ); // 5
		} else {
			players.add( new Player( 5, 0, color++).add( 5,12).add( 4,11).add( 5,11).add( 4,10).add( 5,10).add( 6,10) ); // 0
			players.add( new Player( 9, 3, color++).add( 0, 9).add( 1, 9).add( 2, 9).add( 1, 8).add( 2, 8).add( 1, 7)); // 1
			players.add( new Player( 9, 9, color++).add( 0, 3).add( 1, 3).add( 2, 3).add( 1, 4).add( 2, 4).add( 1, 5)); // 2
			players.add( new Player( 5,12, color++).add( 5, 0).add( 4, 1).add( 5, 1).add( 4, 2).add( 5, 2).add( 6, 2) ); // 3
			players.add( new Player( 0, 9, color++).add( 7, 3).add( 8, 3).add( 9, 3).add( 8, 4).add( 9, 4).add( 8, 5)); // 4			
			players.add( new Player( 0, 3, color++).add( 7, 9).add( 8, 9).add( 9, 9).add( 8, 8).add( 9, 8).add( 8, 7)); // 5
		}
		for (Player player : players) {
			for (Piece piece : player.pieces) {
				ball.set(piece.point);
			}
		}
	}

	/** return Piece corresponding pointed by provided @param point */
	public Piece piece(Move move) {
		return piece(move.point(0));
	}

	
	/** return Piece corresponding pointed by provided @param point */
	public Piece piece(Point point) {
		for (Player player : players) {
			for (Piece p : player.pieces) {
				if (p.point.equals(point)) return p;
			}
		}
		return null;
	}
	
	/** @return true if proposed move step towards @param point p is valid for @param piece. A single translate o jump move, proposed by UI. */ 
	public boolean valid(Piece piece, Point p) {
		return ball.valid(piece.point, p);
	}

	
	/** Actually move @param piece to a given position @param p. Keeping in synch the pieces list and the board 'ball'. 
	 * Validation is do be done elsewhere.
	 */
	public boolean move(Piece piece, Point p) {
		ball.reset( piece.point);
		piece.point = p;
		ball.set(p);
		return true;
	}

	/** @return true if each steps of given @param move is valid */
	public boolean valid(Move move) {
		return ball.valid(move);
	}
	
	/** Validation to be done before... TODO*/
	public boolean play(Move m) {
		Piece piece = piece( m.point(0));
		if (piece==null) return false;
		Point point = m.point( m.points.size()-1);
		return move( piece, point);
	}
	
	
	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Player player : players) {
			for (Piece p : player.pieces) {
				ji[p.point.j][p.point.i] = true;
			}
		}
		String msg = "";
		msg += "----------------game----------------------\n";
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
