package com.tlabs.android.evanova.app.fitting;

import android.content.Context;

import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.fitting.impl.FittingUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class FittingModule {

    private FittingPreferences preferences;

    public FittingModule(Context context) {
        this.preferences = new FittingPreferences(context.getApplicationContext());
    }

    @Provides
    @UserScope
    public FittingPreferences provideFittingPreferences() {
        return this.preferences;
    }

    @Provides
    @UserScope
    public FittingUseCase provideFittingUseCase(final FittingUseCaseImpl useCase) {
        return useCase;
    }
}
