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

import org.scoutant.cc.model.Piece;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class PieceUI extends FrameLayout implements OnTouchListener, OnLongClickListener {
	
	@SuppressWarnings("unused")
	private static final String tag = "activity";
	
	private Resources resources;
	private Drawable square;
	private Canvas canvas;
	/** square size in pixel */
	private int size;

	public Piece piece;
	public int i0;
	public int j0;

	public int i;
	public int j;
	
	private int localX=0;
	private int localY=0;
	
	public static final int[] icons = { R.drawable.green, R.drawable.yellow };
	
	private Paint paint = new Paint();
	
	private Context context;
	private Vibrator vibrator; 
	
	public PieceUI(Context context, Piece piece) {
		super(context);
		this.context = context;
		setWillNotDraw(false);
		setOnLongClickListener(this);
		setOnTouchListener(this);
		resources = context.getApplicationContext().getResources();
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		size = display.getWidth()/20;
		this.piece = piece;
		square = resources.getDrawable( icons[piece.color]);
		paint.setColor(0x99999999);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public PieceUI( Context context, Piece piece, int i, int j){
		this(context, piece);
		i0=i;
		j0=j;
//		setVisibility(INVISIBLE);
	}

	public void place(int i, int j){
		move(i,j);
	}

	public void move(int i, int j) {
		this.i=i;
		this.j=j;
		invalidate();
	}
	

	/** 
	 * Caution : must invoke doLayout() before any invalidate() if i or j happend to be updated! As onDraw wont be called if piece is (was) out of  viewport. 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
	}

	public boolean onLongClick(View v) {
		return true;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		GameView game = (GameView) getParent();
		int action = event.getAction();
		if (game.selected==null) {
		}
    	if (action==MotionEvent.ACTION_DOWN) {
    		localX = (int)event.getX();
    		localY = (int)event.getY();
    		bringToFront();
    	}
    	if (action==MotionEvent.ACTION_MOVE && game.selected==this) {
    		bringToFront();
    	}
    	if (action==MotionEvent.ACTION_UP) {
    	}
    	invalidate();
		return false;
	}
	
	@Override
	public String toString() {
		return "<PieceUI> : (" + this.i + ", " + this.j + ") ; " + piece;
	}

}
