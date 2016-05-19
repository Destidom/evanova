package com.tlabs.android.evanova.app.route;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.route.ui.RouteDisplayActivity;
import com.tlabs.android.evanova.app.route.ui.RouteInputActivity;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {
                EvanovaComponent.class},
        modules = {
                RouteModule.class})
public interface RouteComponent {

    void inject(RouteDisplayActivity activity);
    void inject(RouteInputActivity activity);
}
