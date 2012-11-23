/*
* Copyright (C) 2012- stephane coutant
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

import org.scoutant.Command;
import org.scoutant.CommandListener;
import org.scoutant.cc.model.Move;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class UI extends BaseActivity {
//	@SuppressWarnings("unused")
	private static String tag = "activity";
	public static final int REQUEST_MENU = 90;
	
	private GameView game;
	private TurnMgr turnMgr;
	private Repository repository;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		findViewById(R.id.turn).setOnClickListener(new CommandListener( new StartAICommand()));
		findViewById(R.id.menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startMenu();
			}
		});
		game = (GameView) findViewById(R.id.game);
		ButtonsMgr buttonMgr = new ButtonsMgr(game, findViewById(R.id.ok), new StartAICommand(), findViewById(R.id.cancel));
		game.setButtonMgr( buttonMgr);
		buttonMgr.resize();
		repository = new Repository(this, game);
		initgame();

		repository.restore();
	}
	
	private void newgame() {
		prefs.edit().putBoolean( KEY_GAME_ON, false).commit();
		Log.d(tag, "setting key_game_on to false... ");
		prefs.edit().putInt( NbPlayersActivity.KEY_NB_PLAYERS, -1).commit();
		initgame();
	}
	
	private void startMenu() {
		Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
		startActivityForResult(intent, REQUEST_MENU);
	}
	
	private void initgame() {
		game.reset();
		turnMgr = new TurnMgr( this, (ImageView) findViewById(R.id.turn), game.height/5);
		game.setTurnMgr(turnMgr);
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		getWindow().setFormat( PixelFormat.RGBA_8888);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO manage double back to exit and single back do nothing
		if ( keyCode == KeyEvent.KEYCODE_MENU) {
			startMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Dealing with the “Bitmap Size Exceeds VM Budget” error.
	 * @see http://www.alonsoruibal.com/bitmap-size-exceeds-vm-budget
	 * <p>Just provide @param view.
	 */
	public static void unbindDrawables(View view) {
	    if (view.getBackground() != null) {
	        view.getBackground().setCallback(null);
	    }
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            unbindDrawables(((ViewGroup) view).getChildAt(i));
	        }
	        ((ViewGroup) view).removeAllViews();
	    }
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		repository.save();
	    unbindDrawables( game);
	    System.gc();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MENU) {
			if (resultCode == MenuActivity.RESULT_BACK) {
				game.back();
			}
			if (resultCode == MenuActivity.RESULT_NEW_GAME) { 
				newgame();
				finish();
			}
			if (resultCode == MenuActivity.RESULT_QUIT)  {
				finish();
			}
			if (resultCode == MenuActivity.RESULT_HELP) { /* TODO Help */ }
			if (resultCode == MenuActivity.RESULT_LOVE) { /* TODO launch User review */ }
		}
	}
	
	protected void play() {
		//TODO manage level
		Move move = game.ai.think(turnMgr.player(), 0);
		if (move==null) {
			// for dev only
			turnMgr.update();
		} else {
			game.play(move, true);
		}
		game.init();
	}
	
	private class AITask extends AsyncTask<Integer, Void, Move> {
		@Override
		protected Move doInBackground(Integer... params) {
			Move move = game.ai.think(turnMgr.player(), 0);
			return move;
		}
		@Override
		protected void onPostExecute(Move move) {
			if (move==null) turnMgr.update();
			game.play(move, true);
			game.init();
			int turn = turnMgr.player();
			
			if (ai(turn)) {
				Log.d(tag, "\n"  +"##################################################################################################################");
				Log.d(tag, "thinking for : " + turn);
				new AITask().execute(turn);
			}
		}
	}

	private class StartAICommand implements Command {
		@Override
		public boolean execute() {
			new AITask().execute( turnMgr.player());
			return false;
		}
		
	}
}