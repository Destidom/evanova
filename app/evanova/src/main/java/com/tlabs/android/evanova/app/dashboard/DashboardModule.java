package com.tlabs.android.evanova.app.dashboard;

import com.tlabs.android.evanova.app.dashboard.impl.DashboardUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class DashboardModule {

    @Provides
    public DashboardUseCase provideDashboardUseCase(final DashboardUseCaseImpl useCase) {
        return useCase;
    }
}
