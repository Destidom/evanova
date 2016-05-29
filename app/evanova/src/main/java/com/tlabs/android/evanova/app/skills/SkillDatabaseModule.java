package com.tlabs.android.evanova.app.skills;


import dagger.Module;
import dagger.Provides;

@Module
public class SkillDatabaseModule {

    @Provides
    public SkillDatabaseUseCase provideDatabaseUseCase(final SkillDatabaseUseCaseImpl impl) {
        return impl;
    }
}
