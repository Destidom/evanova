package com.tlabs.android.evanova.app.route;

import com.tlabs.android.evanova.mvp.ActivityView;

import org.devfleet.dotlan.DotlanRoute;

public interface RouteDisplayView extends ActivityView {

    void displayJumps(final DotlanRoute route);

    void displayRoute(final DotlanRoute route, int type);
}
