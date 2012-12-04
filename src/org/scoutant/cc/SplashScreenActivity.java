package org.scoutant.cc;

import org.scoutant.Command;
import org.scoutant.CommandListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;

public class SplashScreenActivity extends DemoActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (gameOn()) {
			new FinishAndReplace().execute();
			return;
		}
		afterDemo = new FinishAndReplace();
		setContentView(R.layout.demo);
		findViewById(R.id.screen).setOnClickListener( new CommandListener( new FinishAndReplace()));

		Display display = getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		int height = size.y;
		game = new DemoGameView(this, height*3/4);
		
		((ViewGroup) findViewById(R.id.container)).addView(game);
		new PlayMove().execute();
	}

	private class FinishAndReplace implements Command {
		@Override
		public void execute() {
			stopPlaying();
			startActivity(new Intent(SplashScreenActivity.this, NbPlayersActivity.class));
			finish();
		}
	}
	
}
