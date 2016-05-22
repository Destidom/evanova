package com.tlabs.android.evanova.app.route.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.routes.RouteDisplayWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

import org.devfleet.dotlan.DotlanRoute;

class RouteDisplayPager extends TabPager {

    private static class RouteDisplayPagerAdapter extends ViewPagerAdapter {

        public RouteDisplayPagerAdapter(final Context context) {
            super(context);

            addView(new RouteDisplayWidget(context), R.string.pager_title_routes_fastest);
            addView(new RouteDisplayWidget(context), R.string.pager_title_routes_high);
            addView(new RouteDisplayWidget(context), R.string.pager_title_routes_low);
        }


        protected void setRoute(final DotlanRoute route, final int index) {
           final RouteDisplayWidget w = getView(index);
            w.setRoute(route);
        }
    }

    private RouteDisplayPagerAdapter adapter;

    public RouteDisplayPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteDisplayPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        setId(R.id.pagerRouteDisplay);

        this.adapter = new RouteDisplayPagerAdapter(getContext());
        setAdapter(this.adapter);
    }

    public void setRoute(final DotlanRoute route, final int index) {
        this.adapter.setRoute(route, index);
    }

    public void setRoute(final DotlanRoute route) {
        this.adapter.setRoute(route, 0);
        this.adapter.setRoute(route, 1);
        this.adapter.setRoute(route, 2);
    }
}
