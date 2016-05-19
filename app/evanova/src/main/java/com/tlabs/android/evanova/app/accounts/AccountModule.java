package com.tlabs.android.evanova.app.accounts;

import com.tlabs.android.evanova.app.accounts.impl.AccountUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class AccountModule {

    @Provides
    public AccountUseCase provideAccountUseCase(final AccountUseCaseImpl impl) {
        return impl;
    }

}
