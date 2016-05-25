package com.tlabs.android.evanova.app.corporations;

import com.tlabs.android.evanova.app.corporations.impl.CorporationUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class CorporationModule {

    @Provides
    public CorporationUseCase provideCorporationUseCase(CorporationUseCaseImpl impl) {
        return impl;
    }
}
