package org.scoutant.cc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverActivity extends BaseActivity {
	
	private LinearLayout container;
	private int rank=1;
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		container = (LinearLayout) findViewById(R.id.container);
		inflater = getLayoutInflater();
		
		bind( R.id.ok, new Finish());
		
		for (int player=0; player<6; player++) {
			if (playing(player)) addRanking(player);
		}
		
	}

	private void addRanking(int player) {
		View view = inflater.inflate(R.layout.ranking_item, container, false);
		TextView tv = (TextView) view.findViewById(R.id.rank);
		tv.setText(""+rank);
		ImageView iv = (ImageView) view.findViewById(R.id.peg);
		iv.setImageResource( PegUI.icons[player]);
		rank++;
		container.addView(view);
	}
	
	

}
