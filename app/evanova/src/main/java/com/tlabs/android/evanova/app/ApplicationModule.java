package com.tlabs.android.evanova.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context provideApplicationContext() {
        return this.application.getApplicationContext();
    }

    @Provides
    public Application provideApplication() {
        return this.application;
    }

}
