package org.scoutant.cc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class HumanVsMachineActivity extends Activity {
	public static final String KEY_NB_PLAYERS = "nb_players";
	
	public static final int RESULT_HELP = 9;
	private static final int REQUEST_UI = 11;
	private SharedPreferences prefs;
	
	private static final int[] ids = { R.id.player_0, R.id.player_1, R.id.player_2, R.id.player_3, R.id.player_4, R.id.player_5 };
	private static final String[] keys = { "h_vs_m_0",  "h_vs_m_1", "h_vs_m_2", "h_vs_m_3", "h_vs_m_4", "h_vs_m_5"};
	private CheckBox[] cbs = new CheckBox[6]; 
	
	private static String tag = "activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.human_vs_machine);
		for (int i=0; i<ids.length; i++) populate( i);
		findViewById(R.id.play).setOnClickListener( new StartListener());
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
//				startActivity( new Intent(this, HelpActivity.class));
				break;
			default: return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void populate(int player) {
		CheckBox view = (CheckBox) findViewById( ids[player]);
		if (view == null) {
			Log.e(tag, "No View ! ");
			return;
		}
		cbs[player] = view;
//		view.setOnClickListener( new PlayerListener( player));
		boolean checked = prefs.getBoolean(keys[player], true);
		view.setChecked(checked);
	}
	
//	private class PlayerListener implements OnClickListener {
//		private int player;
//		private PlayerListener(int player) {
//			this.player = player;
//		}
//		@Override
//		public void onClick(View v) {
//			Log.d(tag, "player : " + player);
//		}
//	}

	private class StartListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			save();
			startActivityForResult( new Intent(getApplicationContext(), UI.class), REQUEST_UI);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == UI.RESULT_QUIT) {
			setResult(UI.RESULT_QUIT);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void save() {
        Editor editor = prefs.edit();
        for (int i=0; i<6; i++) {
        	editor.putBoolean(keys[i], cbs[i].isChecked());
        }
        editor.commit();
	}
	
	
	
}
