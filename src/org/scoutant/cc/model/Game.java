package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;

public class Game implements org.scoutant.Serializable {
	
	@SuppressWarnings("unused")
	private static String tag = "model";

	public List<Player> players = new ArrayList<Player>();
	public Board board;
	public Player player(int index) {
		return players.get(index);
	}
	public List<Move> moves = new ArrayList<Move>();
	public Move last() { return moves.get(moves.size()-1);}
	public boolean pop() {
		if (moves.size() <= 0) return false;
		moves.remove(moves.size()-1);
		return true;
	}

	public Game( ) {
		this( new boolean[] { true, true, true, true, true, true } );
	}

	
	/**
	 * creating players in this order :
	 *<p>    3 
	 *<p> 2     4
	 *<p> 1     5
	 *<p>    0
	 */
	public Game( boolean[] playings ) {
		board = new Board();
		int color=0;
		players.add( new Player( 6, 0, color++).add( 6,16).add( 5,15).add( 6,15).add( 5,14).add( 6,14).add( 7,14).add( 4,13).add( 5,13).add( 6,13).add( 7,13) ); // 0
		players.add( new Player(12, 4, color++).add( 0,12).add( 1,12).add( 2,12).add( 3,12).add( 0,11).add( 1,11).add( 2,11).add( 1,10).add( 2,10).add( 1, 9) ); // 1
		players.add( new Player(12,12, color++).add( 0, 4).add( 1, 4).add( 2, 4).add( 3, 4).add( 0, 5).add( 1, 5).add( 2, 5).add( 1, 6).add( 2, 6).add( 1, 7) ); // 2
		players.add( new Player( 6,16, color++).add( 6, 0).add( 5, 1).add( 6, 1).add( 5, 2).add( 6, 2).add( 7, 2).add( 4, 3).add( 5, 3).add( 6, 3).add( 7, 3) ); // 3
		players.add( new Player( 0,12, color++).add( 9, 4).add(10, 4).add(11, 4).add(12, 4).add( 9, 5).add(10, 5).add(11, 5).add(10, 6).add(11, 6).add(10, 7) ); // 4
		players.add( new Player( 0, 4, color++).add( 9,12).add(10,12).add(11,12).add(12,12).add( 9,11).add(10,11).add(11,11).add(10,10).add(11,10).add(10, 9) ); // 5
		for (int i=0; i<6; i++) {
			Player player = players.get(i);
			if (!playings[i]) player.clear();
			for (Peg peg : player.pegs()) {
				board.set(peg.point);
			}
		}
	}

	/** return Peg corresponding pointed by provided @param point */
	public Peg peg(Move move) {
		return peg(move.point(0));
	}

	
	/** return Peg corresponding pointed by provided @param point */
	public Peg peg(Point point) {
		for (Player player : players) {
			for (Peg p : player.pegs()) {
				if (p.point.equals(point)) return p;
			}
		}
		return null;
	}
	
	/** Actually move @param peg to a given position @param p. Keeping in synch the pegs list and the board 'ball'. 
	 * Validation is do be done elsewhere.
	 */
	public boolean move(Peg peg, Point p) {
		board.reset( peg.point);
		peg.point = p;
		board.set(p);
		return true;
	}

	/** @return true if each steps of given @param move is valid */
	public boolean valid(Move move) {
		return board.valid(move);
	}
	
	/**
	 * Assuming move was previously validated. Play provided move @param m.
	 * @return true if done.   
	 */
	public boolean play(Move move) {
//		Log.d(tag, "playing move " + move);
		Peg peg = peg( move.point(0));
		if (peg==null) return false;
		Point point = move.point( move.points.size()-1);
		boolean done = move( peg, point);
		if (done) moves.add(move);
		return done;
	}
	
	
	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Player player : players) {
			for (Peg p : player.pegs()) {
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

	// TODO manage game over!
	public boolean over() {
		return false;
	}

	@Override
	public String serialize() {
		String str= "";
		for (Move move : moves) {
			str+= move.serialize() +"\n";
		}
		return str;
	}
	// no direct deserialization : we will replay the list of moves instead. To have the UIs in sync with the game state...

	
	
}