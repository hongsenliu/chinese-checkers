package org.scoutant.cc;

import org.scoutant.cc.model.Move;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class UI extends Activity {
	private static String tag = "activity";
	public static final int MENU_ITEM_PLAY = 10;
	public static final int MENU_ITEM_ANIMATE = 20;
	private GameView game;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		game = new GameView( UI.this);
		setContentView( game);
		super.onCreate(savedInstanceState);
	}
	public int turn = 0;

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		getWindow().setFormat( PixelFormat.RGBA_8888);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE, MENU_ITEM_PLAY, Menu.NONE, R.string.play).setIcon( R.drawable.player_play_41_48);
		menu.add(Menu.NONE, MENU_ITEM_ANIMATE, Menu.NONE, "animate").setIcon( android.R.drawable.btn_default);

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
//			peg.setVisibility(View.INVISIBLE);
			Move move = new Move().add(0,12).add(5,2).add(6,5).add(5,8).add(7,13);
			
//			peg.animate(move);
			
//			Animation a = new TranslateAnimation(
//					Animation.ABSOLUTE, -150, Animation.ABSOLUTE, 0,
//					Animation.ABSOLUTE, -300, Animation.ABSOLUTE, 0);					
			Animation a = new TranslateAnimation( -50, -80, -100, -30);
			a.setDuration(700);
			a.setAnimationListener( new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					Animation b = new TranslateAnimation( -80, 0, -30, 0);
					b.setDuration(700);
					peg.startAnimation(b);
				}
			});
			peg.startAnimation(a);
			
//			AnimationSet set = new AnimationSet(false);
//			set.addAnimation(a);
//			set.addAnimation(a);
//			peg.startAnimation(set);
			
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
	
	private void play() {
		Move move = game.ai.think(0, 0);
		game.play(move, false);
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
