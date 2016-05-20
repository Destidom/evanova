package com.tlabs.android.jeeves.views.ui.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;



/** ViewPager with event capture.*/
public class ViewPager extends android.support.v4.view.ViewPager {

	public interface OnPageChangeListener extends android.support.v4.view.ViewPager.OnPageChangeListener {
		
		boolean onPageTap(final ViewPager p);
		
		boolean onPageDoubleTap(final ViewPager p);
		
		boolean onPageLongPress(final ViewPager p);
	
	}
	
	public static class OnPageChangeAdapter implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}

		@Override
		public boolean onPageTap(ViewPager p) {
			return false;
		}

		@Override
		public boolean onPageDoubleTap(ViewPager p) {
			return false;
		}

		@Override
		public boolean onPageLongPress(ViewPager p) {
			return false;
		}
		
	}

	private OnPageChangeListener listener = null;	
	private GestureDetector gestureDetector;
	
	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ViewPager(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
        setOffscreenPageLimit(5);//Default is 1 - we usually have 2 or 3

        this.gestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (null == listener) { 
					return super.onSingleTapConfirmed(e);
				}
				return listener.onPageTap(ViewPager.this);				
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				if (null == listener) {
					return super.onSingleTapConfirmed(e);
				}
				return listener.onPageDoubleTap(ViewPager.this);
			}

			@Override
			public void onLongPress(MotionEvent e) {
				if (null == listener) {
					super.onLongPress(e);
					return;
				}
				listener.onPageLongPress(ViewPager.this);
			}
			
		});
		this.gestureDetector.setIsLongpressEnabled(true);
	//	setPageTransformer(true, new DepthPageTransformer());
	}
	
	public final void setOnPageChangeListener(OnPageChangeListener l) {
		super.setOnPageChangeListener(l);
		this.listener = l;		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean done;
		try {			
			done = gestureDetector.onTouchEvent(event);
			if (!done) {
				done = super.onTouchEvent(event);
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			//this happens on slow devices during super.onTouchEvent();
			//Log.w(LOG, "ViewPager#onTouchEvent(): " + e.getMessage());
			return false;
		}
		return done;
	}

}
