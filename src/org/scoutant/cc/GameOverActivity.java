package org.scoutant.cc;

import java.util.List;

import org.scoutant.cc.model.Game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class GameOverActivity extends BaseActivity {
	
	private LinearLayout container;
	private int rank=0;
	private LayoutInflater inflater;
	private List<Integer> ranking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		container = (LinearLayout) findViewById(R.id.container);
		inflater = getLayoutInflater();
		
		bind( R.id.ok, new Finish());
		
		// TODO process true ranking!
		processRankingOrder();
		
		for (int player=0; player<6; player++) {
			if (playing(player)) addRanking();
		}
		
	}

	private void processRankingOrder() {
		int[] overs = getIntent().getIntArrayExtra("overs");
		ranking = Game.ranking(overs);
	}
	
	private void addRanking() {
		View view = inflater.inflate(R.layout.ranking_item, container, false);
		TextView tv = (TextView) view.findViewById(R.id.rank);
		tv.setText(""+(rank+1));
		ImageView iv = (ImageView) view.findViewById(R.id.peg);
		iv.setImageResource( PegUI.icons[ ranking.get(rank)]);
		rank++;
		container.addView(view);
	}
}
