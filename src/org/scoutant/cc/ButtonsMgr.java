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

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ButtonsMgr {

	protected static final String tag = "ui";
	private View cancel;
	private View ok;

	private GameView game;

	public ButtonsMgr(GameView game, View ok, View cancel) {
		this.game = game;
		this.ok = ok;
		this.ok.setOnClickListener(doOk);
		this.cancel = cancel;
		this.cancel.setOnClickListener(doCancel);
		setVisibility(View.INVISIBLE);
		setOkState( false);
	}
	
	public void setVisibility(int visibility) {
		ok.setVisibility(visibility);
		cancel.setVisibility(visibility);
	}

	protected void setState( View btn, boolean state) {
		btn.setEnabled( state);
		btn.setAlpha( state ? 0.8f : 0.3f );
	}

	public void reset() {
		setOkState(false);
		setVisibility(View.INVISIBLE);
	}
	
	public void setOkState(boolean state) {
		setState(ok, state);
	}

	private OnClickListener doOk = new OnClickListener() {
		public void onClick(View v) {
			game.game.play( game.move);
			game.init();
		}
	};
	private OnClickListener doCancel = new OnClickListener() {
		public void onClick(View v) {
			Log.d(tag, "cancel...");
			game.init();
		}
	};
}