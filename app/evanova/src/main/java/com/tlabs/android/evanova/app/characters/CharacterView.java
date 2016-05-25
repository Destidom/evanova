package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterView {

    public static final String EXTRA_CHAR_ID = CharacterView.class.getSimpleName() + ".charID";

    void showCharacter(final EveCharacter character);

    void updateCharacter(final EveCharacter character);
}
