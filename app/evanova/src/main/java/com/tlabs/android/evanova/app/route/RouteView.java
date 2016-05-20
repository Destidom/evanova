package com.tlabs.android.evanova.app.route;

import com.tlabs.android.evanova.mvp.ActivityView;

import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;

import java.util.List;

public interface RouteView extends ActivityView {

    void showOptions(final List<DotlanOptions> options);

    void showRoute(final DotlanRoute route);

    void showRoute(final DotlanRoute route, int type);
}
