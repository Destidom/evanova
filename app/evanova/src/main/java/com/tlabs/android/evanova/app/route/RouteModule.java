package com.tlabs.android.evanova.app.route;


import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.route.impl.RouteUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    @Provides
    @UserScope
    public RouteUseCase provideRouteUseCase(final RouteUseCaseImpl impl) {
        return impl;
    }
}
