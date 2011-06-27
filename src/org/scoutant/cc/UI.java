package org.scoutant.cc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class UI extends Activity {
	private static String tag = "activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView( new GameView( UI.this));
		super.onCreate(savedInstanceState);
	}
	public int turn = 0;

}
