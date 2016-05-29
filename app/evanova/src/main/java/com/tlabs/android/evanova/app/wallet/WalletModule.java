package com.tlabs.android.evanova.app.wallet;


import dagger.Module;
import dagger.Provides;

@Module
public class WalletModule {

    @Provides
    public WalletUseCase provideWalletUseCase(final WalletUseCaseImpl impl) {
        return impl;
    }
}
