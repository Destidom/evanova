package com.tlabs.android.evanova.app.route;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.route.ui.RouteActivity;

import dagger.Component;

@UserScope
@Component(
        dependencies = {
                EvanovaComponent.class},
        modules = {
                RouteModule.class})
public interface RouteComponent {

    void inject(RouteActivity activity);

}
