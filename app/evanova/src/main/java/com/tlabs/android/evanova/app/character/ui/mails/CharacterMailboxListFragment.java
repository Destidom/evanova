package com.tlabs.android.evanova.app.character.ui.mails;

import com.tlabs.android.evanova.app.character.CharacterMailView;
import com.tlabs.android.evanova.app.character.ui.CharacterFragment;
import com.tlabs.android.jeeves.model.EveCharacter;

public class CharacterMailboxListFragment extends CharacterFragment implements CharacterMailView {

    @Override
    protected void onCharacterChanged(EveCharacter character) {
        presenter.setView(this);
    }
}
