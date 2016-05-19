package com.tlabs.android.evanova.app.route.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.route.DaggerRouteComponent;
import com.tlabs.android.evanova.app.route.RouteInputView;
import com.tlabs.android.evanova.app.route.RouteModule;
import com.tlabs.android.evanova.app.route.presenter.RouteInputPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import org.devfleet.dotlan.DotlanOptions;

import java.util.List;

import javax.inject.Inject;

public class RouteInputActivity extends BaseActivity implements RouteInputView {

    @Inject
    RouteInputPresenter presenter;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void displayOptions(List<DotlanOptions> options) {
      /*  getFragment().setRoutes(options);
        if (!options.isEmpty()) {
            getFragment().setRoute(options.get(0));
        }*/
    }

}

