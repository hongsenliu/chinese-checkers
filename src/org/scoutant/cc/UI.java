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
import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
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
import android.widget.Toast;

public class UI extends BaseActivity {
//	@SuppressWarnings("unused")
	private static String tag = "activity";
	public static final int REQUEST_MENU = 90;
	
	private GameView game;
	private TurnMgr turnMgr;
	private Repository repository;
	private Command startAI = new StartAI();
	private Command mayStartAI = new MayStartAI();
	private int resultCode = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		if (Game.DEV) findViewById(R.id.turn).setOnClickListener(new CommandListener( startAI));
		findViewById(R.id.menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startMenu();
			}
		});
		game = (GameView) findViewById(R.id.game);
		ButtonsMgr buttonMgr = new ButtonsMgr(game, findViewById(R.id.ok), new MayStartAI(), findViewById(R.id.cancel));
		game.setButtonMgr( buttonMgr);
		game.setMayStartAI( mayStartAI);
		
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
		turnMgr = new TurnMgr( this, (ImageView) findViewById(R.id.turn));
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
	
	
	/**
	 * When resuming from an interrupted sequence of AI, here we launch AI till next human player.
	 * But not for feature 'back on move'.
	 */
	@Override
		protected void onResume() {
			super.onResume();
			if (resultCode!= MenuActivity.RESULT_BACK) mayStartAI.execute();
			resultCode = 0;
		}
	
	@Override
		protected void onPause() {
			game.pauseAnimations();
			super.onPause();
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
		this.resultCode = resultCode ;  
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
			if (resultCode == MenuActivity.RESULT_HELP) { 
				startActivity( new Intent(this, HelpActivity.class)); 
			}
			if (resultCode == MenuActivity.RESULT_LOVE) { 
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.scoutant.cc")); 
				startActivity(intent);
			}
		}
	}
	
	private class AITask extends AsyncTask<Integer, Void, Move> {
		@Override
		protected Move doInBackground(Integer... params) {
			if (Game.LOG) Log.d(tag, "\n"  +"####################################################################################");
			int turn = turnMgr.player(); 
			if (Game.LOG) Log.d(tag, "thinking for : " + turn);
			if (game.game.over()) return null; // to prevent looping if no human player...
			Move move = game.ais[turn].think();
			return move;
		}
		@Override
		protected void onPostExecute(Move move) {
			int turn = turnMgr.player();
			if (move==null) turnMgr.update();
			if (game.game.over()) return;
			game.play(move, true, mayStartAI);
			if (move!=null && game.game.over(turn)) {
				Log.d(tag, "player " + turn +" is now over!");
				// TODO nive message with button 'continue' and 'menu'...
				Toast.makeText(UI.this , "Player " + turn + " is over!", Toast.LENGTH_SHORT).show();
				if (game.game.over()) {
					Toast.makeText(UI.this , "GAME is just OVER!", Toast.LENGTH_LONG).show();
				}
			}
			game.init();
		}
	}

	private class StartAI implements Command {
		@Override
		public void execute() {
			new AITask().execute( turnMgr.player());
		}
		
	}
	private class MayStartAI implements Command {
		@Override
		public void execute() {
			int turn = turnMgr.player();
			if (ai( turn)) startAI.execute();
			else { // human
				if (game.game.over(turn)) { // human player is over let's trigger next player!
					turnMgr.update();
					mayStartAI.execute();
				}
			}
		}
		
	}
}