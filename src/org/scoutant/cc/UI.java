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
import android.view.Menu;
import android.view.MenuItem;
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
	
	// TODO remove std menu stuff 
	public static final int MENU_ITEM_PLAY = 10;
	public static final int MENU_ITEM_NEW = 20;
	public static final int MENU_ITEM_PLAY12 = 12;
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
				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				startActivityForResult(intent, REQUEST_MENU);
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

	// TODO remove menu
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE, MENU_ITEM_PLAY, Menu.NONE, "back").setIcon( R.drawable.left_128);
		menu.add(Menu.NONE, MENU_ITEM_PLAY12, Menu.NONE, "play 12").setIcon( R.drawable.player_play_41_128);
		menu.add(Menu.NONE, MENU_ITEM_NEW, Menu.NONE, "new").setIcon( R.drawable.restart_128);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		int id = item.getItemId();
		if (id == MENU_ITEM_PLAY) {
			game.back();
		}
		if (id == MENU_ITEM_NEW) {
			newgame();
		}
		if (id == MENU_ITEM_PLAY12) {
			for (int i=0; i<12; i++) {
				play();
			}
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_SEARCH:
				play();
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
			if (resultCode == MenuActivity.RESULT_BACK) game.back();
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

//findViewById(R.id.turn).setOnClickListener(new OnClickListener() {
//@Override
//public void onClick(View v) {
////	play();
//	new AITask().execute( turnMgr.player());
//}
//});
