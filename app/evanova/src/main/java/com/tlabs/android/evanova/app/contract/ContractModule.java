package com.tlabs.android.evanova.app.contract;

import com.tlabs.android.evanova.app.contract.impl.ContractUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ContractModule {

    @Provides
    public ContractUseCase provideContractUseCase(ContractUseCaseImpl impl) {
        return impl;
    }
}
