package com.tlabs.android.jeeves.views.ui.animation;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

//@from http://www.wiseandroid.com/post/2010/08/10/Making-a-simple-custom-Android-UI-animation.aspx
//@author Ivan.Memruk 
public class BlinkAnimation extends Animation {
	private final int totalBlinks;
	private final boolean finishOff;

	public BlinkAnimation() {
		this(1, 3000L, false);
		setRepeatCount(INFINITE);
	}
	
	public BlinkAnimation(int totalBlinks) {
		this(totalBlinks, 3000L, false);
	}
	
	public BlinkAnimation(int totalBlinks, long duration) {
		this(totalBlinks, duration, false);
	}
	
	private BlinkAnimation(int totalBlinks, long duration, boolean finishOff) {
		super();
		this.totalBlinks = totalBlinks;
		this.finishOff = finishOff;
		
		setInterpolator(new LinearInterpolator());
	    setDuration(duration < 500L ? 500L : duration);
	}

	@Override
    public boolean willChangeBounds() {
        return false;
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return false;
    }

	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float period = interpolatedTime * totalBlinks * 3.14f + (finishOff ? 3.14f / 2 : 0);
		t.setAlpha((float)Math.abs(Math.cos(period)));
	}
	
	
}
