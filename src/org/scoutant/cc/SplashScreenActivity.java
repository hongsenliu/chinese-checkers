package org.scoutant.cc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.scoutant.Command;
import org.scoutant.CommandListener;
import org.scoutant.cc.model.Move;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;

public class SplashScreenActivity extends BaseActivity {
	
	private static final String tag = "activity";
	private DemoGameView game;
	private int current=0;
	private List<Move> moves = new ArrayList<Move>();
	private Command finish = new Finish();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (gameOn()) {
			startActivityForResult(new Intent(SplashScreenActivity.this, NbPlayersActivity.class), 0);
			finish();
			return;
		}
		Display display = getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		int height = size.y;
		game = new DemoGameView(this, height*3/4);
		setContentView(R.layout.demo);
		findViewById(R.id.container).setOnClickListener( new CommandListener(finish));
		
		((ViewGroup) findViewById(R.id.container)).addView(game);
		load();
		new PlayMove().execute();
	}
	
	public void load() {
		try {
			InputStream fis = getResources().openRawResource(R.raw.game);
			BufferedReader reader = new BufferedReader( new InputStreamReader(fis));
			reader.readLine();
			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null)   {
				Move move = Move.deserialize(line);
				moves.add(move);
			}
		} catch (Exception e) {
			Log.e(tag, "yep error is :", e);
		}
	}

	
	private class PlayMove implements Command {
		@Override
		public void execute() {
			if (current>= moves.size()) finish.execute();
			else game.play( moves.get(current++), true, this);
		}
	}

	private class Finish implements Command {
		@Override
		public void execute() {
			startActivityForResult(new Intent(SplashScreenActivity.this, NbPlayersActivity.class), 0);
			finish();
		}
	}
}
