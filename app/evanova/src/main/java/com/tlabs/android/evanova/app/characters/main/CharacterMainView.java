package com.tlabs.android.evanova.app.characters.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

interface CharacterMainView extends ActivityView {

    void setCharacter(final EveCharacter character);

    void updateCharacter(final EveCharacter character);

    void showCharacterDetails();
}
