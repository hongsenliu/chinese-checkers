package org.scoutant.cc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


public abstract class BaseActivity extends Activity {
	
	@SuppressWarnings("unused")
	private static String tag = "activity";
	public static final String KEY_GAME_ON = "game_on";
	protected SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	
	protected boolean gameOn() {
		return prefs.getBoolean( KEY_GAME_ON, false);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.d(tag, "onResume" + this.getClass().getCanonicalName());
//		Log.d(tag, "game on ? "+ gameOn());
//		Log.d(tag, "nb players : " + prefs.getInt(NbPlayersActivity.KEY_NB_PLAYERS, 99));
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Log.d(tag, "onActivityResult" + this.getClass().getCanonicalName() +  " - requestCode : " + requestCode +", resultCode : " + resultCode);
//		Log.d(tag, "game on ? "+ gameOn());
//	}
	
}
