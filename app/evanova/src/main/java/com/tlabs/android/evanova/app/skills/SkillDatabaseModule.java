package com.tlabs.android.evanova.app.skills;


import com.tlabs.android.evanova.app.skills.impl.SkillDatabaseUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SkillDatabaseModule {

    @Provides
    public SkillDatabaseUseCase provideDatabaseUseCase(final SkillDatabaseUseCaseImpl impl) {
        return impl;
    }
}
