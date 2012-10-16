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

import org.scoutant.cc.model.AI;
import org.scoutant.cc.model.Board;
import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Peg;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Player;
import org.scoutant.cc.model.Point;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * @author scoutant
 * http://commons.wikimedia.org/wiki/Category:Round_icons
 */
public class GameView extends FrameLayout  {

	public static int sizeI = Board.sizeI;
	public static int sizeJ = Board.sizeJ;
	
	private static String tag = "view";
	private static String touch = "touch";
	public int size; 
	public ButtonsView buttons;
	public Peg selected; // ball
	public Point pointed;  // board target point
	public Move move; // current target move in construction
	
	public Game game;
	public AI ai;
	public UI ui;
	public SharedPreferences prefs;
	public boolean thinking=false;
	public int dI;
	public int dJ;
	private Bitmap hole ;
	protected int diameter;
	private Bitmap iconSelected; 
	private Bitmap iconPointed; 
	private Paint paint = new Paint();
	private FrameLayout.LayoutParams layoutParams = null;
	private ButtonsMgr buttonMgr;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		setWillNotDraw(false);

		setBackgroundColor(Color.RED);
//		setBackgroundResource(R.layout.linear_gradient);
		getBackground().setDither(true);
		
		game = new Game();
		ai = new AI(game);

		processSize();
		
		buttons = new ButtonsView(context);
		addView( buttons);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        hole = BitmapFactory.decodeResource(context.getResources(), R.drawable.steel);
        iconSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.ring2);
        iconPointed = BitmapFactory.decodeResource(context.getResources(), R.drawable.ring3);

		paint.setStrokeWidth(0.2f);
		paint.setColor(Color.BLACK);

		for (Player player : game.players) {
			for (Peg peg : player.pegs()) {
				addView( new PegUI(context, peg, this));
			}
		}
	}

	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setLayoutParams( layoutParams);
	}
	
	/** In equilateral triangle we have : 1² = (1/2)² + h²  => h = sqrt(3)/2 = 08660254 */
	private void processSize() {
		Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		android.graphics.Point outSize = new android.graphics.Point();
		display.getSize(outSize);
//		if (true) {
		if (prefs.getBoolean("two-player", false)) { // portrait mode with buttons up or down according to who is the turn.
			((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			dI = outSize.x/sizeI;
			dJ = Double.valueOf(0.8660254*dI).intValue();
			// TODO test ok
			layoutParams = new FrameLayout.LayoutParams(outSize.x, sizeJ*dJ, Gravity.TOP);
			layoutParams.topMargin = (outSize.y-sizeJ*dJ)/2;
		} else { // standard : landscape mode : one button on each side. 
			dJ = Double.valueOf( outSize.y/sizeJ).intValue();
			dI = Double.valueOf(dJ / 0.8660254).intValue();
			layoutParams = new FrameLayout.LayoutParams(sizeI*dI, outSize.y, Gravity.TOP);
			layoutParams.leftMargin = (outSize.x-sizeI*dI)/2;
		}
		Log.i(tag, "width : " + outSize.x + ", height : " + outSize.y+ ", dI : " + dI + ", dJ : " + dJ);
		diameter = Double.valueOf( 0.96*dI).intValue();
	}
	
	public PegUI findPeg(Peg peg) {
		if (peg==null) return null;
		PegUI found=null;
		for (int i=0; i<getChildCount(); i++) {
			View v = getChildAt(i);
			if (v instanceof PegUI) {
				found = (PegUI) v;
//				if (found.peg.color == peg.color && found.peg.equals(peg)) return found;
				if (found.peg.equals(peg)) return found;
			}
		}
		return null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (selected!=null) return false;
		doTouch(event);
		return true;
	}
	
	/** Initialize state so as to accept a fresh new move*/
	public void init() {
		buttons.reset();
		buttonMgr.reset();
		selected = null;
		pointed = null;
		move = null;
		invalidate();
	}

	public void doTouch(MotionEvent event) {
		int action = event.getAction(); 
		if (action==MotionEvent.ACTION_DOWN) {
			Pixel e = new Pixel( event);
			Point p = point(e);
			if (!Board.hole(p)) return;
			// coordinate of the center of corresponding cell
			Pixel o = pixel(p);
			Log.d(touch, "down on " + e + ", p is : " + p + ", center o : " + o);
			Point n = new Point (p.i, p.j + ( e.y<o.y ? -1 : + 1));
			// when click in a corner we may be nearer a row up or below 
			Point s = p;
			// Yes, 'Neighbor' may actually be closer, if it is a hole it is worth a check
			if ( Board.hole(n)) {
				Pixel oN = pixel(n);
				int dO = Pixel.distance(e, o);
				int dN = Pixel.distance(e, oN);
				Log.d(touch, "neighbour " + n + ", oN " + oN + ", dist O : " + dO + ", dist N : " + dN);
				if (dN<dO) Log.i(tag, "Neighboor refining with : " + n);
				s = ( dN<dO ? n : p);
			}
			Log.i(touch, "touched : " + s);
			if (selected==null || (pointed==null && game.board.is(s))) select( s);
			else point( s);
			invalidate();
		}
	}

	
	/** User pretend to select one of his balls */
	private void select(Point p) {
		if (!game.board.is(p)) return;
		// retrieve ball under selection
		// TODO ensure ball actually is one of his. Or no need and consider case : selected==null...
		selected = game.peg(p);
		Log.i(tag, "selected is now : " + selected);
		buttons.setVisibility(VISIBLE);
		buttonMgr.setVisibility(VISIBLE);
	}
	
	/** User pretend to point a free hole as target for next step */
	private void point(Point p) {
		if (game.board.is(p)) return;
		if (move==null) move = new Move(selected);
		// if user goes back in his move path, we just pop the last 2 points...
		if ( p.equals( move.penultima())) {
			move.pop();
			return;
		}
		move.add(p);
		Log.d(touch, "proposed move length : " + move.points.size());
		pointed = p;
		boolean possible = game.valid( move);
		Log.d(touch, "possible move : " + possible);
		if (!possible) move.pop();
		if (move.points.size()>1) buttons.setOkState( true);
		if (move.points.size()>1) buttonMgr.setOkState( true);
	}
	

	/** @return the cell Point in which Pixel @param l happens to be in */
	private Point point( Pixel l) {
		int j = l.y/dJ;
		// dI/2 offset for odd lines : 
		int oI = (j%2==0 ? 0 : dI/2);
		int i = (l.x-oI)/dI;
		return new Point (i, j);
	}

	/** @return the center of the hole identified by provided @param point */
	protected Pixel pixel(Point p) {
		// dI/2 offset for odd lines : 
		int oI = (p.j%2==0 ? 0 : dI/2);
		return new Pixel(dI/2 + p.i*dI+oI, dJ/2 +p.j*dJ);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
	}
	
	private void drawBoard(Canvas canvas){
//		Log.d(tag, "onDraw");
		for (int j=0; j<sizeJ; j++){
			for (int i=0; i< sizeI; i++) {
				Pixel l = pixel(new Point(i, j));
				if (Board.hole.is(i,j)) {
					canvas.drawBitmap(hole, null, toSquare(l, diameter), null);
				}
			}
		}
		if (selected!=null) {
			canvas.drawBitmap( iconSelected, null, toSquare( pixel(selected.point), diameter*12/10), null);
		}
		if (move==null) return;
		for (Point p : move.points) {
			canvas.drawBitmap( iconPointed, null, toSquare( pixel(p), diameter*12/10), null);
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

	public void play(Move move, boolean animate) {
		Log.d(tag, "playing move " + move);
		if (move==null) return;
		Peg start = game.peg(move.point(0));
		PegUI peg = findPeg(start);
		
		boolean done = game.play(move);
		if (done) {
			peg.animate(move);
			invalidate();
		}
	}



	public void setButtonMgr(ButtonsMgr buttonMgr) {
		this.buttonMgr = buttonMgr; 
		
	}
	
	
}