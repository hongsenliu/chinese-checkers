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

package org.scoutant.cc;

import org.scoutant.cc.model.Board;
import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Piece;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Player;
import org.scoutant.cc.model.Point;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * 
 * @author scoutant
 * http://commons.wikimedia.org/wiki/Category:Round_icons
 */
public class GameView extends FrameLayout  {
	private static String tag = "view";
	private static String touch = "touch";
	public int size; 
	public ButtonsView buttons;
	public Piece selected; // ball
	public Point pointed;  // board target point
	public Move move; // current target move in construction
	
	public Game game;
//	public AI ai = new AI(game);
	public static int[] icons = { R.drawable.red, R.drawable.springgreen, R.drawable.purple, R.drawable.gold, R.drawable.pink, R.drawable.turquoise};
	public Bitmap[] balls = new Bitmap[6];
	public UI ui;
	public SharedPreferences prefs;
	public boolean thinking=false;
	private int dI;
	private int dJ;
	private Bitmap hole ;
	private int diameter;
	private Bitmap iconSelected; 
	private Bitmap iconPointed; 
	private Paint paint = new Paint();
	
	/** In equilateral triangle we have : 1² = (1/2)² + h² */
	public GameView(Context context) {
		super(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		ui = (UI) context;
		setWillNotDraw(false);
		setLayoutParams( new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, Gravity.TOP));
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		Log.i(tag, "width : " + display.getWidth() + ", height : " + display.getHeight());

		game = new Game(false);
		
		dI = display.getWidth()/10;
		dJ = new Double(0.866*dI).intValue();
//		diameter = new Double( 0.8*dI).intValue();
		diameter = new Double( 0.96*dI).intValue();
		Log.i(tag, "width : " + display.getWidth() + ", height : " + display.getHeight() + ", dI : " + dI + ", dJ : " + dJ);
		
		buttons = new ButtonsView(context);
		addView( buttons);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        hole = BitmapFactory.decodeResource(context.getResources(), R.drawable.steel);
        iconSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow);
        iconPointed = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow_2);

		paint.setStrokeWidth(0.4f);
		paint.setColor(Color.WHITE);

		for (int color=0; color<6; color++) {
			balls[color] = BitmapFactory.decodeResource(context.getResources(), icons[color]);
		}
	}
	
	public Pixel e;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (selected!=null) return false;
		doTouch(event);
		return true;
	}
	
	/** Initialize state so as to accept a fresh new move*/
	public void init() {
		buttons.reset();
		selected = null;
		pointed = null;
		move = null;
		invalidate();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(tag, "KEY : " + keyCode);
		return true;
	}
	
	public void doTouch(MotionEvent event) {
		int action = event.getAction(); 
    	if (action==MotionEvent.ACTION_DOWN) {
    		e = new Pixel( event);
    		// TODO downY -= 25, if status bar left available. always that offset??.
    		Log.d(touch, "down on " + e);
    		Point p = point(e);
    		Log.d(touch, "down : " + p);
    		if (!p.hole()) return;
    		
    		// coordinate of the center of corresponding cell
    		Pixel o = pixel(p);
    		Log.d(touch, "o " + o);
    		int nI = p.i;
    		if (p.j%2==0) {
    			nI += ( e.x<o.x ? -1 : 0);
    		} else {
    			nI += ( e.x<o.x ? 0 : 1);    			
    		}
    		Point n = new Point (nI, p.j + ( e.y<o.y ? -1 : + 1));
    		// qd on clique dans un coin, on est parfois plus près de la bille de la rangés de dessous ou dessus...
    		Log.d(touch, "n " + n);
    		Point s = p;
    		// If n happen to be a hole it may be closer...
    		if ( n.hole()) {
	    		Pixel oN = pixel(n);
	    		Log.d(touch, "oN " + oN);
	    		int dO = Pixel.distance(e, o);
	    		int dN = Pixel.distance(e, oN);
	    		Log.d(touch, "dist O : " + dO);
	    		Log.d(touch, "dist N : " + dN);
	    		s = ( dN<dO ? n : p);
    		}
			Log.d(tag, "touched : " + s);
			if (selected==null || (pointed==null && game.ball.is(s))) select( s);
			else point( s);
			invalidate();
    	}
    	if (action==MotionEvent.ACTION_MOVE ) {
    	}
    	if (action==MotionEvent.ACTION_UP ) {
    	}
	}

	
	/** User pretend to select one of her balls */
	private void select(Point p) {
		if (!game.ball.is(p)) return;
		// retrieve ball under selection
		selected = game.piece(p);
		buttons.setVisibility(VISIBLE);
	}
	
	/** User pretend to point a free hole as target for her selected ball */
	private void point(Point p) {
		if (game.ball.is(p)) return;
		pointed = p;
		// TODO validate against move and not against p!
		boolean possible = game.valid(selected, p);
		Log.d(tag, "possible move : " + possible);
		buttons.setOkState( possible);
		if (!possible) return;
		if (move==null) move = new Move(selected);
		move.add(p);
		Log.d(tag, "move length : " + move.points.size());
	}
	
	
	/** @return the cell Point in which Pixel @param l happens to be in */
	private Point point( Pixel l) {
		// miniboard : -dI/2 comme offset global et oI comme offset contextuel
		int j = l.y/dJ;
		int oI = (j%2==0 ? 0 : dI/2);
		int i = (l.x+dI/2-oI)/dI;
		return new Point (i, j);
	}

	/** @return the center of the hole identified by provided @param point */
	private Pixel pixel(Point p) {
		// -dI/2 is the global offset for the miniboard and (dI/2-1, dJ/2-1) is the standard offset for center of the cell.
		// and +dI/2 is contextual offset for I on odd lines...
		int oI = (p.j%2==0 ? 0 : dI/2);
		int x = -dI/2+dI/2-1+p.i*dI+oI;
		return new Pixel(x, dI/2-1+p.j*dJ);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		canvas.drawBitmap(bitmap, null, new Rect(0, 0, 80, 80), null);
		drawMiniBoard(canvas);
	}
	
	/** 
	 * Mini board sizeI=11, but only 10 most left balls visible, And with dI/2 offset! 
	 */
	private void drawMiniBoard(Canvas canvas){
//		Log.d(tag, "sizeI : " + Board.sizeI);
		for (int j=0; j<=13; j++){
			canvas.drawLine(0, j*dJ+1, 10*dI, j*dJ+1, paint);
		}
		for (int i=0; i<= 10; i++) {
			canvas.drawLine(i*dI, 0, i*dI, 13*dJ, paint);
		}
		for (int j=0; j<13; j++){
			for (int i=0; i< 10; i++) {
				Pixel l = pixel(new Point(i, j));
				if (Board.hole.is(i,j)) {
					canvas.drawBitmap(hole, null, toSquare(l, diameter), null);
				}
			}
		}
		if (selected!=null) {
			canvas.drawBitmap( iconSelected, null, toSquare( pixel(selected.point), diameter*13/10), null);
		}
		for (Player player : game.players) {
			for (Piece piece : player.pieces) {
				canvas.drawBitmap( balls[player.color], null, toSquare( pixel( piece.point), diameter), null);
			}
		}
		
//		if (pointed!=null) {
//			canvas.drawBitmap( iconPointed, null, toSquare( pixel(pointed), diameter/2), null);
//		}
		if (move==null) return;
		for (Point p : move.points) {
			canvas.drawBitmap( iconPointed, null, toSquare( pixel(pointed), diameter/2), null);
			
		}
	}
	
	/**
	 * @return a Rect instance representing a square centered on (@param x, @param y) with @param length  
	 */
	public static Rect toSquare(int x, int y, int length) {
		return new Rect( x-length/2, y-length/2, x+length/2-1, y+length/2-1);
	}
	
	/**
	 * @return a Rect instance representing a square centered on (@param x, @param y) with @param length  
	 */
	public static Rect toSquare(Pixel l, int length) {
		return new Rect( l.x-length/2, l.y-length/2, l.x+length/2, l.y+length/2);
	}

	
//	public PieceUI findPiece(Move move) {
//		if (move.points.size()<1) return null;
//		return findPiece(move.point(0));
//	}
//	public PieceUI findPiece(Point point) {
//		// TODO
//		return null;
//	}
//	
//	public void play(Move move) {
//		if (move==null) return;
////		Piece piece = game.piece(move);
//		PieceUI ui = findPiece( move);
//		boolean done = game.play(move);
////		if (done) ui.place(move.i, move.j);
//		invalidate();
//	}
//	
//
//	public boolean replay(List<Move> moves) {
//		for (Move move : moves) {
//			PieceUI ui = findPiece(move);
////			ui.piece.reset(piece);
//			play(move);
//		}
//		return true;
//	}	
}