package org.scoutant.cc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


public abstract class BaseActivity extends Activity {
	
	protected static final String[] keys = { "h_vs_m_0",  "h_vs_m_1", "h_vs_m_2", "h_vs_m_3", "h_vs_m_4", "h_vs_m_5"};
	
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
	

	public static boolean[] playings(Context context) {
		int nbPlayers = PreferenceManager.getDefaultSharedPreferences(context).getInt(NbPlayersActivity.KEY_NB_PLAYERS, 6);
		switch( nbPlayers) { 
			case 2 : return new boolean[] { true, false, false, true , false, false};
			case 3 : return new boolean[] { true, false, true , false, true , false};
			case 6 : return new boolean[] { true, true , true , true , true , true };
			case -1: return new boolean[] {false, false, false, false, false, false};
			default : throw new IllegalArgumentException( "nb Players must be : 2, 3 or 6");
		}
	}
	
	protected boolean playing(int player) {
		return playings(this)[player];
	}

	public static boolean[] ais(Context context) {
		boolean playings[] = new boolean[6];
		for (int i=0; i<6; i++) playings[i] = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(keys[i], true);
		return playings; 
	}

	protected boolean ai(int player) {
		return ais(this)[player];
	}
	
}