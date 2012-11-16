package org.scoutant.cc;

import org.scoutant.cc.model.Peg;

import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TurnMgr {
	private ImageView view;
	private int d;
	private BaseActivity activity;

	public TurnMgr(BaseActivity activity, ImageView turnView, int d) {
		this.activity = activity;
		this.view = turnView;
		this.d = d;
		updateView();
	}

	private int turn = 0;
	public int player() { return turn;}
	
	/** @return true is current player is AI  */
	public boolean isAI() { return activity.ai(turn); }

	/** @return true is current player is human  */
	public boolean ishuman() { return !isAI(); }
	
	public void update() {
		turn++;
		turn = turn%6;
		if (activity.playing(turn)){
			updateView();
		} else update();
	}

	// TODO add a fade-out and fade-in animation?
	private void updateView(){
		int resId = PegUI.icons[turn];
		view.setImageResource(resId);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(d, d, Gravity.RIGHT|Gravity.BOTTOM);
		layoutParams.rightMargin = 20;
		layoutParams.bottomMargin = 20;
		view.setLayoutParams( layoutParams);
	}

	public boolean allowed(Peg peg){
		return ( peg.color == turn);
	}

	public void pop() {
		turn+=5;
		turn = turn%6;
		if (activity.playing(turn)){
			Log.d("activity", "turn is now : " + turn);
			updateView();
		} else pop(); 
	}
}
