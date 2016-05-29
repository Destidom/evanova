package com.tlabs.android.evanova.app.market;

import dagger.Module;
import dagger.Provides;

@Module
public class MarketOrdersModule {

    @Provides
    public MarketOrdersUseCase provideMarketOrdersUseCase(final MarketOrdersUseCaseImpl impl) {
        return impl;
    }
}
