package com.tlabs.android.jeeves.views.routes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.views.R;

import org.devfleet.dotlan.DotlanRoute;

public class RouteDisplayWidget extends FrameLayout {

    private RecyclerView gridView;
    private RecyclerView listView;

    private DotlanRoute route;

    public RouteDisplayWidget(Context context) {
        super(context);
        init();
    }

    public RouteDisplayWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteDisplayWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.jeeves_view_route_display, this);
        this.gridView = (RecyclerView)view.findViewById(R.id.j_routeDisplayGrid);
        this.gridView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.gridView.setAdapter(new RouteDisplayGridAdapter());

        this.listView = (RecyclerView)view.findViewById(R.id.j_routeDisplayList);
        this.listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.listView.setAdapter(new RouteDisplayListAdapter());

        this.route = null;
    }

    public void setRoute(final DotlanRoute route) {
        this.route = route;

        ((AbstractDisplayRouteAdapter<?>)this.gridView.getAdapter()).setRoute(route);
        ((AbstractDisplayRouteAdapter<?>)this.listView.getAdapter()).setRoute(route);
    }

    public DotlanRoute getRoute() {
        return route;
    }
}
