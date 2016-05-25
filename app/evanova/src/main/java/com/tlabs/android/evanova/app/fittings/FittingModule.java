package com.tlabs.android.evanova.app.fittings;

import android.content.Context;

import com.tlabs.android.evanova.app.fittings.impl.FittingUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class FittingModule {

    private FittingPreferences preferences;

    public FittingModule(Context context) {
        this.preferences = new FittingPreferences(context.getApplicationContext());
    }

    @Provides
    public FittingPreferences provideFittingPreferences() {
        return this.preferences;
    }

    @Provides
    public FittingUseCase provideFittingUseCase(final FittingUseCaseImpl useCase) {
        return useCase;
    }
}
