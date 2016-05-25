package com.tlabs.android.evanova.app.characters;

import dagger.Module;
import dagger.Provides;

@Module
public class CharacterModule {

    @Provides
    public CharacterUseCase provideCharacterUseCase(CharacterUseCaseImpl impl) {
        return impl;
    }
}
