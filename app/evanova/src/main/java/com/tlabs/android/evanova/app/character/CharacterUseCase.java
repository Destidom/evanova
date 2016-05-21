package com.tlabs.android.evanova.app.character;

import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

public interface CharacterUseCase {

    List<EveCharacter> loadCharacters();

    EveCharacter loadCharacter(final long charID);

}
