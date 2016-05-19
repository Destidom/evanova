package com.tlabs.android.evanova.app.route.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.route.DaggerRouteComponent;
import com.tlabs.android.evanova.app.route.RouteDisplayView;
import com.tlabs.android.evanova.app.route.RouteModule;
import com.tlabs.android.evanova.app.route.presenter.RouteDisplayPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import org.devfleet.dotlan.DotlanRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class RouteDisplayActivity extends BaseActivity implements RouteDisplayView {
    private static final Logger LOG = LoggerFactory.getLogger(RouteDisplayActivity.class);

    public static final String EXTRA_WAYPOINTS = RouteDisplayActivity.class.getName() + ".waypoints";//array
    public static final String EXTRA_JUMP = RouteDisplayActivity.class.getName() + ".options";//custom format

    @Inject
    RouteDisplayPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerRouteComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .routeModule(new RouteModule())
                .build()
                .inject(this);

        this.presenter.setView(this);
        this.presenter.loadRoutes(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void displayJumps(DotlanRoute route) {
    /*    getFragment().setRoute(route, 0);
        getFragment().setRoute(route, 1);
        getFragment().setRoute(route, 2);*/
    }

    @Override
    public void displayRoute(DotlanRoute route, int type) {
   //     getFragment().setRoute(route, type);
    }
}
