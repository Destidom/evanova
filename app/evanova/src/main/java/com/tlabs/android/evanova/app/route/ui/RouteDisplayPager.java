package com.tlabs.android.evanova.app.route.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.routes.RouteDisplayWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;

import org.devfleet.dotlan.DotlanRoute;

import java.util.ArrayList;
import java.util.List;

public class RouteDisplayPager extends ViewPager {

    static class RouteDisplayPagerAdapter extends PagerAdapter {
        private final List<RouteDisplayWidget> views;
        private final Context context;

        public RouteDisplayPagerAdapter(final Context context) {
            this.views = new ArrayList<>(3);
            views.add(new RouteDisplayWidget(context));
            views.add(new RouteDisplayWidget(context));
            views.add(new RouteDisplayWidget(context));

            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RouteDisplayWidget view =  views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return this.context.getResources().getString(R.string.pager_title_routes_fastest);
                case 1:
                    return this.context.getResources().getString(R.string.pager_title_routes_high);
                case 2:
                    return this.context.getResources().getString(R.string.pager_title_routes_low);
                default:
                    return "";
            }
        }

        protected void setRoute(final DotlanRoute route, final int index) {
            this.views.get(index).setRoute(route);
        }
    }

    public RouteDisplayPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RouteDisplayPager(Context context) {
        super(context);
    }
    /*
    @Override
    protected RouteDisplayPagerAdapter createAdapter(Bundle savedInstanceState) {
        return new RouteDisplayPagerAdapter(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPagerListener(page -> Activities.invalidateTitle(getActivity(), false));
    }

    @Override
    protected int getPagerID() {
        return R.id.pagerRoutesDisplay;
    }

    protected void setRoute(final DotlanRoute route, final int index) {
        getPagerAdapter().setRoute(route, index);
    }*/
}
