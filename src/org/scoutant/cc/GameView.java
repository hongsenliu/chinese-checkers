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

import java.util.List;

import org.scoutant.cc.model.Board;
import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Point;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GameView extends FrameLayout  {
	private static String tag = "view";
	public int size; 
	public ButtonsView buttons;
	public PieceUI selected;
	
	public Game game;
//	public AI ai = new AI(game);
//	public static int[] icons = { R.drawable.bol_rood, R.drawable.bol_groen, R.drawable.bol_blauw, R.drawable.bullet_ball_glass_yellow};
	private Drawable[] dots = new Drawable[4]; 
	public UI ui;
	public SharedPreferences prefs;
	public boolean thinking=false;
//	private int radius;
	private int dI;
	private int dJ;
	private Bitmap hole ;
	private int diameter;
	private Bitmap ball; 
	private Paint paint = new Paint();
	
	/** In equilateral triangle we have : 1² = (1/2)² + h² */
	public GameView(Context context) {
		super(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		ui = (UI) context;
		setWillNotDraw(false);
		setLayoutParams( new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, Gravity.TOP));
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Log.i(tag, "width : " + display.getWidth() + ", height : " + display.getHeight());

		game = new Game(false);
		
		dI = display.getWidth()/10;
		dJ = new Double(0.866*dI).intValue();
		diameter = new Double( 0.8*dI).intValue();
//		radius = new Double( 0.8*dI/2).intValue();
		Log.i(tag, "dI : " + dI + ", dJ : " + dJ);
		
		buttons = new ButtonsView(context);
		addView( buttons);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        hole = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
        ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow);

		paint.setStrokeWidth(1.3f);
		paint.setColor(Color.WHITE);
	
	}
	
	public float downX;
	public float downY;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (selected!=null) return false;
		doTouch(event);
		return true;
	}

	
	public void doTouch(MotionEvent event) {
		int action = event.getAction(); 
    	if (action==MotionEvent.ACTION_DOWN) {
    		downX=event.getRawX();
    		downY=event.getRawY();
    		// TODO pour emulateur seulement !
    		downY -= 25;
    		Log.d(tag, "down on : " + downX +", " + downY);
    		int j = new Double((downY)/dJ).intValue();
    		// Attention si j impair, décallage de dI/2 
			int oI = (j%2==0 ? 0 : dI/2);
			// miniboard : -dI/2 comme offset global
    		int i = new Double((downX+dI/2-oI)/dI).intValue();
    		// attention decalage de ligne j pour j impaire
    		Point p = new Point(i,j);
    		Log.d(tag, "down : " + p);
    		if (p.hole()) {
				game.ball.set(p);
				invalidate();
    		}
    	}
    	if (action==MotionEvent.ACTION_MOVE ) {
    	}
    	if (action==MotionEvent.ACTION_UP ) {
    	}
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
		int oI;
		Log.d(tag, "sizeI : " + Board.sizeI);
		for (int j=0; j<=13; j++){
			canvas.drawLine(0, j*dJ+1, 10*dI, j*dJ+1, paint);
		}
		for (int i=0; i<= 10; i++) {
			canvas.drawLine(i*dI, 0, i*dI, 13*dJ, paint);
		}

		
		for (int j=0; j<13; j++){
			oI = (j%2==0 ? 0 : dI/2);
			for (int i=0; i< 10; i++) {
				if (Board.hole.is(i,j)) {
					// -dI/2 c'est l'offset global pour miniboard
					// (dI/2-1, dJ/2-1) c'est l'offset pour le centrage.
					canvas.drawBitmap(hole, null, toSquare(-dI/2+dI/2-1+i*dI+oI, dI/2-1+j*dJ, diameter), null);
				}
				if (game.ball.is(i,j)) {
					canvas.drawBitmap(ball, null, toSquare(-dI/2+dI/2-1+i*dI+oI, dI/2-1+j*dJ, diameter), null);
				}
			}
		}
	}
	
	/**
	 * @return a Rect instance representing a square centered on (@param x, @param y) with @param length  
	 */
	public static Rect toSquare(int x, int y, int length) {
		// TODO bas if length not multiple of 2?
		return new Rect( x-length/2, y-length/2, x+length/2-1, y+length/2-1);
	}
	

	public PieceUI findPiece(Move move) {
		if (move.points.size()<1) return null;
		return findPiece(move.point(0));
	}
	public PieceUI findPiece(Point point) {
		// TODO
		return null;
	}
	
	public void play(Move move) {
		if (move==null) return;
//		Piece piece = game.piece(move);
		PieceUI ui = findPiece( move);
		boolean done = game.play(move);
//		if (done) ui.place(move.i, move.j);
		invalidate();
	}
	

	public boolean replay(List<Move> moves) {
		for (Move move : moves) {
			PieceUI ui = findPiece(move);
//			ui.piece.reset(piece);
			play(move);
		}
		return true;
	}	
}


//ImageView gameBoard = new ImageView(context);
//gameBoard.setImageResource(R.drawable.board_5_1);
//addView(gameBoard);


//canvas.drawBitmap(bitmap, null, new Rect( -dI/2+i*dI+oI, j*dJ, -dI/2+i*dI+oI+diameter, j*dJ+diameter), null);
//canvas.drawBitmap(bitmap, null, toSquare(dI/2+i*dI+oI, dI/2+j*dJ, diameter), null);
