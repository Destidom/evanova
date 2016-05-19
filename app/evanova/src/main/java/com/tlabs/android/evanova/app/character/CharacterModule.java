package com.tlabs.android.evanova.app.character;

import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.character.impl.CharacterUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class CharacterModule {

    @Provides
    @UserScope
    public CharacterUseCase provideCharacterUseCase(CharacterUseCaseImpl impl) {
        return impl;
    }
}
