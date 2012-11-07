package org.scoutant.cc;

import org.scoutant.cc.model.Move;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
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
	public static final int MENU_ITEM_PLAY = 10;
	public static final int MENU_ITEM_ANIMATE = 20;
	private GameView game;
	private TurnMgr turnMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		game = (GameView) findViewById(R.id.game);
		ButtonsMgr buttonMgr = new ButtonsMgr(game, findViewById(R.id.ok), findViewById(R.id.cancel));
		game.setButtonMgr( buttonMgr);
		buttonMgr.resize();
		turnMgr = new TurnMgr( (ImageView) findViewById(R.id.turn), game.height/5);
		game.setTurnMgr(turnMgr);
		findViewById(R.id.turn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				play();
			}
		});
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		getWindow().setFormat( PixelFormat.RGBA_8888);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE, MENU_ITEM_PLAY, Menu.NONE, R.string.play).setIcon( R.drawable.player_play_41_48);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		int id = item.getItemId();
		if (id == MENU_ITEM_PLAY) {
			play();
		}
		if (id == MENU_ITEM_ANIMATE) {
			final PegUI peg = game.findPeg( game.game.player(0).peg(9));
			Move move = new Move().add(8,13).add(6,8).add(10,8);
			new MoveAnimation(peg, move).start();
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
		game.play(move, false);
		game.init(true);
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
	    unbindDrawables( game);
	    System.gc();
	}

}