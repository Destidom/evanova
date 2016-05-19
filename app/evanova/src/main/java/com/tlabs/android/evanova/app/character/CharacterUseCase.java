package com.tlabs.android.evanova.app.character;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

public interface CharacterUseCase {

    List<EveCharacter> loadCharacters();

    EveCharacter loadCharacter(final long charID);

    EveAccount loadAccount(final long charID);

}
