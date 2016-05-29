package com.tlabs.android.evanova.app.contracts;

import dagger.Module;
import dagger.Provides;

@Module
public class ContractModule {

    @Provides
    public ContractUseCase provideContractUseCase(ContractUseCaseImpl impl) {
        return impl;
    }
}
