package com.tlabs.android.jeeves.views.ui.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final List<View> views;
    private final List<Integer> titles;

    private final Context context;

    public ViewPagerAdapter(final Context context) {
        this.context = context;
        this.views = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    public void addView(final View view, final int rTitle) {
        this.views.add(view);
        this.titles.add(rTitle);
    }

    public final <T extends View> T getView(final int position) {
        return (T)this.views.get(position);
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        final View view = this.views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public final int getCount() {
        return this.views.size();
    }

    @Override
    public final CharSequence getPageTitle(int position) {
       return context.getResources().getString(this.titles.get(position));
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public final List<View> getViews() {
        return Collections.unmodifiableList(this.views);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //must be implemented.
    }
}
