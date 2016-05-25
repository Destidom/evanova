package com.tlabs.android.evanova.app.routes;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.routes.ui.RouteActivity;

import dagger.Component;

@Component(
        dependencies = {
                ApplicationComponent.class},
        modules = {
                RouteModule.class})
public interface RouteComponent {

    void inject(RouteActivity activity);

}
