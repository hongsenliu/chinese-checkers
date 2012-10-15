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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class TurnView extends FrameLayout {

	protected static final String tag = "ui";
	
	private Context context;
	private ImageButton ok;

	private GameView game;

	public TurnView(Context context) {
		super(context);
		this.context = context;
		setVisibility(VISIBLE);
		int h = 128;
		setLayoutParams( new FrameLayout.LayoutParams(h, h, Gravity.TOP|Gravity.RIGHT));
		ok = button(R.drawable.kservices_1436_48, doOk);
		addView(ok);
	}
	
	
	private ImageButton button(int src, OnClickListener l) {
		ImageButton btn = new ImageButton(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		btn.setLayoutParams(params);
		btn.setImageDrawable(context.getResources().getDrawable( src));
		btn.setBackgroundColor(Color.TRANSPARENT);
		btn.setOnClickListener(l);
		return btn;
		
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		game = (GameView) getParent();
	}
	
	private OnClickListener doOk = new OnClickListener() {
		public void onClick(View v) {
//			((Activity) game.getContext()).openOptionsMenu();
			((UI) game.getContext()).play();
		}
	};
}