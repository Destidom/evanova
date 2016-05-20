package com.tlabs.android.evanova.app.route.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.routes.RouteInputWidget;
import com.tlabs.android.jeeves.views.routes.RouteListWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;

import org.devfleet.dotlan.DotlanOptions;

import java.util.List;

class RouteInputPager extends ViewPager {

    public interface Listener {
        void onRouteSelected(final DotlanOptions options);
    }

    private static class RouteInputPagerAdapter extends PagerAdapter {
        private final Context context;

        private final RouteListWidget listView;
        private final RouteInputWidget inputView;

        public RouteInputPagerAdapter(Context context) {

            this.context = context;
            this.listView = new RouteListWidget(context);
            this.listView.setListener(new RouteListWidget.Listener() {
                @Override
                public void onRouteSelected(DotlanOptions options) {
                    RouteInputPagerAdapter.this.onRouteSelected(options);
                }

                @Override
                public void onRouteChanged() {
                    RouteInputPagerAdapter.this.onRouteChanged();
                }
            });

            this.inputView = new RouteInputWidget(context);
            this.inputView.setListener(options -> onRouteSelected(options));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            switch (position) {
                case 1: {
                    container.addView(this.listView);
                    return this.listView;
                }
                case 0:
                default: {
                    container.addView(this.inputView);
                    return this.inputView;
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return context.getResources().getString(R.string.activity_routes_saved);
                case 0:
                default:
                    return context.getResources().getString(R.string.activity_routes_input);
            }
        }

        protected void setRoute(final DotlanOptions options) {
            this.inputView.setRoute(options);
        }

        protected void setRoutes(final List<DotlanOptions> options) {
            this.listView.setRoutes(options);
        }

        protected List<DotlanOptions> getRoutes() {
            return this.listView.getRoutes();
        }

        protected void onRouteSelected(final DotlanOptions options) {
        }

        protected void onRouteChanged() {
        }
    }


    private RouteInputPagerAdapter adapter;
    private Listener listener;

    public RouteInputPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteInputPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        setId(R.id.pagerRouteInput);
        this.adapter = new RouteInputPagerAdapter(getContext()) {
            @Override
            protected void onRouteSelected(DotlanOptions options) {
                if (null != listener) {
                    listener.onRouteSelected(options);
                }
            }
        };
        setAdapter(this.adapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setRoute(final DotlanOptions options) {
        this.adapter.setRoute(options);
    }

    public void setRoutes(final List<DotlanOptions> saved) {
        this.adapter.setRoutes(saved);
    }

}
