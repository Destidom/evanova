package com.tlabs.android.evanova.app.characters.training;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterTrainingView extends ActivityView {

    void setCharacter(final EveCharacter character);
}
