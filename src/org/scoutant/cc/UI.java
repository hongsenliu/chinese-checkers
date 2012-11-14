package org.scoutant.cc;

import org.scoutant.cc.model.Move;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
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

public class UI extends Activity {
	@SuppressWarnings("unused")
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
		findViewById(R.id.turn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				play();
			}
		});
		findViewById(R.id.menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				 openOptionsMenu();
				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				startActivityForResult(intent, REQUEST_MENU);
			}
		});
		game = (GameView) findViewById(R.id.game);
		ButtonsMgr buttonMgr = new ButtonsMgr(game, findViewById(R.id.ok), findViewById(R.id.cancel));
		game.setButtonMgr( buttonMgr);
		buttonMgr.resize();
		repository = new Repository(this, game);
		newgame();

		repository.restore();
	}
	
	private void newgame() {
		game.reset();
		turnMgr = new TurnMgr( (ImageView) findViewById(R.id.turn), game.height/5);
		game.setTurnMgr(turnMgr);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		repository.restore();
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
//			repository.save();
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
	protected void onStop() {
//		repository.save();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		repository.save();
	    super.onDestroy();
	    // TODO save game on exit!
	    unbindDrawables( game);
	    System.gc();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(tag, "requestCode : " + requestCode + ", resultCode : " + resultCode);
		if (requestCode == REQUEST_MENU) {
			if (resultCode == MenuActivity.RESULT_BACK) game.back();
			if (resultCode == MenuActivity.RESULT_NEW_GAME) newgame();
			if (resultCode == MenuActivity.RESULT_QUIT) finish();
			if (resultCode == MenuActivity.RESULT_HELP) { /* TODO Help */ }
			if (resultCode == MenuActivity.RESULT_LOVE) { /* TODO launch User review */ }
		}
	}
}