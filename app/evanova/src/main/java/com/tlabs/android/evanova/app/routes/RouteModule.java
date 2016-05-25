package com.tlabs.android.evanova.app.routes;


import com.tlabs.android.evanova.app.routes.impl.RouteUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    @Provides
    public RouteUseCase provideRouteUseCase(final RouteUseCaseImpl impl) {
        return impl;
    }
}
