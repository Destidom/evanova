package com.tlabs.android.evanova.app.characters.training;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.characters.CharacterUseCase;

import javax.inject.Inject;

public class CharacterTrainingPresenter extends EvanovaActivityPresenter<CharacterTrainingView> {

    private final CharacterUseCase useCase;

    @Inject
    public CharacterTrainingPresenter(Context context, CharacterUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void startView(Intent intent) {
        showLoading(true);
        subscribe(() -> useCase.loadCharacter(ownerOf(intent)), c-> {
           showLoading(false);
           if (null != c) {
               getView().setCharacter(c);
           }
        });
    }
}
