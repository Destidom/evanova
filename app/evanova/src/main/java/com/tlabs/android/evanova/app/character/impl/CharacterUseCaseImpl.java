package com.tlabs.android.evanova.app.character.impl;

import com.tlabs.android.evanova.app.character.CharacterUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CharacterUseCaseImpl implements CharacterUseCase {

    private final ContentFacade content;

    @Inject
    public CharacterUseCaseImpl(ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<EveCharacter> loadCharacters() {
        final List<EveCharacter> characters = new ArrayList<>();
        for (Long id: content.listCharacters()) {
            characters.add(content.getCharacter(id, false));
        }
        return characters;
    }

    @Override
    public EveCharacter loadCharacter(long id) {
        return content.getCharacter(id, true);
    }

    @Override
    public EveAccount loadAccount(long charID) {
        return content.getOwnerAccount(charID);
    }
}
