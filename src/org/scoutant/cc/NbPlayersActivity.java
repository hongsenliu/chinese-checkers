package org.scoutant.cc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class NbPlayersActivity extends Activity {
	public static final String KEY_NB_PLAYERS = "nb_players";
	
	public static final int RESULT_HELP = 9;
	private SharedPreferences prefs;

	private static String tag = "activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.nb_players);
		bind(R.id.players_2, 2);
		bind(R.id.players_3, 3);
		bind(R.id.players_6, 6);
	}
	
	
	private void bind(int viewId, int nbPlayers) {
		View view = findViewById(viewId);
		if (view == null) {
			Log.e(tag, "No View ! ");
			return;
		}
		view.setOnClickListener( new NbPlayersListener( nbPlayers)); 
		
	}
	
	private class NbPlayersListener implements OnClickListener {
		private int nb;

		private NbPlayersListener(int nb) {
			this.nb = nb;
		}

		@Override
		public void onClick(View v) {
			save(nb);
			startActivityForResult( new Intent(getApplicationContext(), HumanVsMachineActivity.class), 0);
		}
	}

	private void save(int nb) {
        Editor editor = prefs.edit();
        editor.putInt(KEY_NB_PLAYERS, nb);
        editor.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == UI.RESULT_QUIT) finish();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
