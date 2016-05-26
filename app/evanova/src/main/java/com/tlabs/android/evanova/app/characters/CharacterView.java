package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterView {

    void showCharacter(final EveCharacter character);

    void updateCharacter(final EveCharacter character);
}
