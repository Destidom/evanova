package com.tlabs.android.jeeves.views.ui.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.tlabs.android.jeeves.views.R;

public class TabPager extends FrameLayout {

    public interface Listener {

        void onPageSelected(int page);
    }

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    private TabPager.Listener listener;

    public TabPager(Context context) {
        super(context);
        init();
    }

    public TabPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public final void setPagerId(final int id) {
        this.pager.setId(id);
    }

    public final int getCurrentItem() {
        return this.pager.getCurrentItem();
    }

    public final void setCurrentItem(final int index) {
        this.pager.setCurrentItem(index);
    }

    public void setAdapter(final PagerAdapter adapter) {
        this.pager.setAdapter(adapter);
        this.tabs.setViewPager(this.pager);
    }

    public final PagerAdapter getAdapter() {
        return this.pager.getAdapter();
    }

    public final void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_tabpager, this);

        this.pager = (ViewPager)findViewById(R.id.jeeves_views_tabs_PagerView);
        this.tabs = (PagerSlidingTabStrip)findViewById(R.id.jeeves_views_tabs_PagerTabStrip);
        this.tabs.setTextColor(Color.WHITE);
        
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int page) {
                if (null == listener) {
                    return;
                }
                listener.onPageSelected(page);
            }
        });
    }
}
