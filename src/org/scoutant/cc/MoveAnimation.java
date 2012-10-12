package org.scoutant.cc;

import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Point;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class MoveAnimation {
	private static final int DURATION = 800;

	private View view;
	private int x=0;
	private int y=0;
	private Animation animation;

	public MoveAnimation(View view) {
		this.view = view;
	}

	public MoveAnimation add(int dx, int dy) {
		if (animation==null) {
			animation = buildAnimation(dx, dy);
		} else {
			animation.setAnimationListener( new AppendAnimation(dx, dy));
		}
		return this;
	}
	
	public void start() {
		view.startAnimation(animation);
	}
	
	protected Animation buildAnimation(int dx, int dy) {
		Animation a= new TranslateAnimation(x, dx, y, dy);
		a.setDuration(DURATION);
		a.setFillAfter(true);
		x += dx;
		y += dy;
		return a;
	}
	
	private class AppendAnimation implements AnimationListener {
		private int dx;
		private int dy;
		protected AppendAnimation(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			Animation b = buildAnimation(dx, dy);
			view.startAnimation(b);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		@Override
		public void onAnimationStart(Animation animation) {
		}
	}
	
//	public MoveAnimation add(Point p) {
//		
//		return this;
//	}
	
	// asuming peg actually is at position corresponding to first point in move...
	public MoveAnimation( PegUI peg, Move move) {
		this(peg);
		Point from = move.first();
		for (int k=1; k<move.points.size(); k++) {
			Point to = move.point(k);
			int dx = (to.i - from.i)*peg.game.dI;
			int dy = (to.j - from.j)*peg.game.dJ;
			this.add(dx, dy);
		}
	}
}
