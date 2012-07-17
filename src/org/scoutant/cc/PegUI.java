package org.scoutant.cc;

import java.util.List;

import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Peg;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Point;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class PegUI extends ImageView {
	public static int[] icons = { R.drawable.red, R.drawable.green, R.drawable.pink, R.drawable.blue, R.drawable.violet, R.drawable.orange};
//	public static int[] ins = { R.anim.red_in, R.anim.push_left_in,R.anim.push_left_in, R.anim.blue_in, R.anim.push_left_in, R.anim.push_left_in}; 
	protected Peg peg;
	private int diameter;
	private GameView game;
//	private Animation wave;

	public PegUI(Context context) {
		super(context);
	}
	
	public PegUI(Context context, Peg peg, GameView game) {
		this(context);
		this.peg = peg;
		this.game = game;
//		wave = AnimationUtils.loadAnimation(context, ins[peg.color]);
		this.diameter = game.diameter*9/10;
		setImageBitmap( BitmapFactory.decodeResource(context.getResources(), icons[peg.color]));
		doLayout();
		}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		doLayout();
	}
	
	private void doLayout() {
		LayoutParams params = new LayoutParams(diameter, diameter, Gravity.TOP);
		Pixel pixel = game.pixel( peg.point);
		Rect rect = toSquare(pixel, diameter);
		params.setMargins(rect.left, rect.top, rect.right, rect.bottom);
		setLayoutParams(params);
	}
	
	@Override
	public String toString() {
		return peg.toString();
	}
	/**
	 * @return a Rect instance representing a square centered on (@param x, @param y) with @param length  
	 */
	public static Rect toSquare(Pixel l, int length) {
		return new Rect( l.x-length/2, l.y-length/2, l.x+length/2, l.y+length/2);
	}

	/**
	 * Animate the Peg so as to move along the path corresponding to provided
	 * @param move
	 */
	public void animate(Move move) {
		if (move==null) return;
		List<Point> points = move.points;
		if (points==null || points.size()< 2) return;
		AnimationSet anim = new AnimationSet(false);
		for (int k=1; k<points.size(); k++) {
			Pixel from	= game.pixel( points.get(k-1));
			Pixel to	= game.pixel( points.get(k));
			// TODO try true for performances...
			Animation a = new TranslateAnimation(
					Animation.ABSOLUTE, from.x, 
					Animation.ABSOLUTE, to.x, 
					Animation.ABSOLUTE, from.y, 
					Animation.ABSOLUTE, to.y);
			a.setDuration(3000);
			anim.addAnimation( a);
		}
		this.startAnimation(anim);
	}

	/**
	 * Adjust the pegs position according to played
	 * @param move 
	 */
	public void move(Move move) {
		peg.point = move.last();
		invalidate();
	}
}