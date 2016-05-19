package com.tlabs.android.jeeves.modules;

import org.devfleet.dotlan.DotlanService;
import org.devfleet.dotlan.impl.DotlanServiceImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class EveDotlanModule {

    @Provides
    public DotlanService provideDotlan() {
        return new DotlanServiceImpl();
    }

}
