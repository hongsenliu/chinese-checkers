package org.scoutant.cc;

import org.scoutant.CommandListener;

import android.os.Bundle;
import android.view.ViewGroup;

public class HelpActivity extends DemoActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		findViewById(R.id.back).setOnClickListener( new CommandListener(finish));
		game = new DemoGameView(this, 350);
		((ViewGroup) findViewById(R.id.container)).addView(game);
		new PlayMove().execute();
	}

}
