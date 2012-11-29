package org.scoutant.cc;

import org.scoutant.Command;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;


public abstract class BaseActivity extends Activity {
	
	protected static final String[] keys = { "h_vs_m_0",  "h_vs_m_1", "h_vs_m_2", "h_vs_m_3", "h_vs_m_4", "h_vs_m_5"};
	
//	private static String tag = "activity";
	public static final String KEY_GAME_ON = "game_on";
	protected SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu ( final android.view.Menu menu ) {
		getMenuInflater().inflate(R.menu.menu_help, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected ( final MenuItem item ) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
			case R.id.menu_item_help:
				startActivity( new Intent(this, HelpActivity.class));
				break;
			default: return super.onOptionsItemSelected(item);
		}
		return true;
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

	protected class Finish implements Command {
		@Override
		public void execute() {
			finish();
		}
	}
	protected class DoNothing implements Command {
		@Override
		public void execute() {
		}
	}

}