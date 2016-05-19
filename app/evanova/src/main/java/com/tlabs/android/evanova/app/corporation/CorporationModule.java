package com.tlabs.android.evanova.app.corporation;

import com.tlabs.android.evanova.app.corporation.impl.CorporationUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class CorporationModule {

    @Provides
    public CorporationUseCase provideCorporationUseCase(CorporationUseCaseImpl impl) {
        return impl;
    }
}
