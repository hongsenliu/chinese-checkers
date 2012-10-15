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

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class ButtonsView extends FrameLayout {

	protected static final String tag = "ui";
	
	private Context context;
	private ImageButton cancel;
	private ImageButton ok;

	private GameView game;

	public ButtonsView(Context context) {
		super(context);
		this.context = context;
		setVisibility(INVISIBLE);
		int h = 128;
		setLayoutParams( new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, h, Gravity.TOP));
		cancel = button(R.drawable.cancel_128, doCancel, 0);
		addView(cancel );
		ok = button(R.drawable.checkmark_128, doOk, 1);
		addView(ok);
		setOkState( false);
	}
	
	
	private ImageButton button(int src, OnClickListener l, int position) {
		ImageButton btn = new ImageButton(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
		if (position==0) params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		if (position==1) params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		btn.setLayoutParams(params);
		btn.setImageDrawable(context.getResources().getDrawable( src));
		btn.setBackgroundColor(Color.TRANSPARENT);
		btn.setOnClickListener(l);
		return btn;
		
	}

	protected void setState( ImageButton btn, boolean state) {
		btn.setEnabled( state);
		btn.setAlpha( state ? 200 : 50 );
	}

	public void reset() {
		setOkState(false);
		setVisibility(INVISIBLE);
	}
	
	public void setOkState(boolean state) {
		setState(ok, state);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		game = (GameView) getParent();
	}
	
	private OnClickListener doOk = new OnClickListener() {
		public void onClick(View v) {
//			Log.d(tag, "'ok' being pressed, let's play the move!...");
			game.game.play( game.move);
			game.init();
////				game.ui.turn = (piece.piece.color+1)%4;
////				if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ai", true)) {
//////					game.ui.think(game.ui.turn);
//			}
		}
	};
	private OnClickListener doCancel = new OnClickListener() {
		public void onClick(View v) {
			Log.d(tag, "cancel...");
			game.init();
		}
	};
}