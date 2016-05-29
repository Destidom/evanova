package com.tlabs.android.evanova.app.routes;


import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    @Provides
    public RouteUseCase provideRouteUseCase(final RouteUseCaseImpl impl) {
        return impl;
    }
}
