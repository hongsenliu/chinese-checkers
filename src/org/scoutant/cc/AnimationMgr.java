package org.scoutant.cc;

import java.util.ArrayList;
import java.util.List;

import org.scoutant.cc.model.Log;

public class AnimationMgr {
	private static final String tag = "animation";
//	private Queue<MoveAnimation> queue = new ArrayBlockingQueue<MoveAnimation>(100, true); 
	private List<MoveAnimation> queue = new ArrayList<MoveAnimation>(); 
	public void add(MoveAnimation anim) {
		queue.add(anim);
		if (queue.size()==1) { 
			anim.start();
		}
		Log.d(tag, "animation queue size : " + queue.size());
	}
	public void done() {
		if (queue.isEmpty()) return;
		queue.remove(0);
		if (queue.isEmpty()) return;
		MoveAnimation anim = queue.get(0);
		if (anim!=null) anim.start();
	}
}
