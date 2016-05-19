package com.tlabs.android.evanova.app.character;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterView extends ActivityView {

    void showMainView(final EveCharacter character);

    void showDetails(final EveCharacter character);

    void showTraining(final EveCharacter character);
}
