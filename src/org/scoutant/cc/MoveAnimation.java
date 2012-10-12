package org.scoutant.cc;

import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Point;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class MoveAnimation {
//	private static String tag = "animation";
	private static final int DURATION = 800;

	private PegUI peg;
	private int x=0;
	private int y=0;
	private Animation animation;

	private Point first;
	private int Dx;
	private int Dy;

	public MoveAnimation(PegUI view) {
		this.peg = view;
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
		peg.startAnimation(animation);
	}

	
	/**
	 * Peg actually was all ready moved by the system, let's replay that with an animation fir user to have time to watch it.
	 * We first go back to first point position
	 */
	
	protected Animation buildAnimation(int dx, int dy) {
//		Animation a= new TranslateAnimation(x, dx, y, dy);
		Animation a= new TranslateAnimation(x-Dx, dx-Dx, y-Dy, dy-Dy);
		// TODO calculation duration against move lenght? So as to have a constant pace?
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
			peg.startAnimation(b);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		@Override
		public void onAnimationStart(Animation animation) {
		}
	}
	
	// assuming peg actually is at position corresponding to first point in move...
	public MoveAnimation( PegUI peg, Move move) {
		this(peg);
		this.first = move.first();
		Point from = first;
		Dx= -dx( move.last(), first);
		Dy= -dy( move.last(), first);
		for (int k=1; k<move.points.size(); k++) {
			Point to = move.point(k);
			this.add( dx(from,to), dy(from,to));
		}
	}
	
	private int dx(Point from, Point to) {
		return peg.game.pixel(to).x - peg.game.pixel(from).x; 
	}
	private int dy(Point from, Point to) {
		return peg.game.pixel(to).y - peg.game.pixel(from).y; 
	}
	
}
