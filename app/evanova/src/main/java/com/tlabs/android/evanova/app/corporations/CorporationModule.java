package com.tlabs.android.evanova.app.corporations;

import dagger.Module;
import dagger.Provides;

@Module
public class CorporationModule {

    @Provides
    public CorporationUseCase provideCorporationUseCase(CorporationUseCaseImpl impl) {
        return impl;
    }
}
