package com.tlabs.android.evanova.app.launcher;

import com.tlabs.android.evanova.app.launcher.impl.LauncherUseCaseImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LauncherModule {

    @Provides
    @Singleton
    public LauncherUseCase provideLauncherUseCase(LauncherUseCaseImpl impl) {
        return impl;
    }
}
