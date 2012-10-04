package org.scoutant.cc;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class NestedAnimationListener  implements AnimationListener {
	private View view;
	private Animation next;
	
	public NestedAnimationListener(View view, Animation next) {
		this.view = view;
		this.next = next;
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
	}
	@Override
	public void onAnimationStart(Animation animation) {
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		view.startAnimation( next);
		
	}

}
