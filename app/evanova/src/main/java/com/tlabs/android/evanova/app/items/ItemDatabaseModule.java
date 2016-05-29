package com.tlabs.android.evanova.app.items;


import dagger.Module;
import dagger.Provides;

@Module
public class ItemDatabaseModule {

    @Provides
    public ItemDatabaseUseCase provideDatabaseUseCase(final ItemDatabaseUseCaseImpl impl) {
        return impl;
    }
}
