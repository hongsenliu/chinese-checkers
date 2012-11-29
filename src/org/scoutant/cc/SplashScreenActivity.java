package org.scoutant.cc;

import org.scoutant.Command;
import org.scoutant.CommandListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;

public class SplashScreenActivity extends DemoActivity {
	
	private static final String tag = "activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish = new FinishAndReplace();
		if (gameOn()) {
			finish.execute();
			return;
		}
		setContentView(R.layout.demo);
		findViewById(R.id.container).setOnClickListener( new CommandListener(finish));
		((ViewGroup) findViewById(R.id.container)).addView(game);

		Display display = getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		int height = size.y;
		game = new DemoGameView(this, height*3/4);
		
		new PlayMove().execute();
	}

	private class FinishAndReplace implements Command {
		@Override
		public void execute() {
			startActivityForResult(new Intent(SplashScreenActivity.this, NbPlayersActivity.class), 0);
			finish();
		}
	}

}
