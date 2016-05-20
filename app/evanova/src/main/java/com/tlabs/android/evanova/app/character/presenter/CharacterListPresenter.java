package com.tlabs.android.evanova.app.character.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.character.CharacterListView;
import com.tlabs.android.evanova.app.character.CharacterUseCase;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.jeeves.model.EveAccount;

import javax.inject.Inject;

public class CharacterListPresenter extends EvanovaActivityPresenter<CharacterListView> {

    private final CharacterUseCase useCase;

    @Inject
    public CharacterListPresenter(Context context, CharacterUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(CharacterListView view) {
        super.setView(view);
        setBackgroundDefault();
        view.setLoading(true);
        
        subscribe(() -> useCase.loadCharacters(), characters -> {
            getView().showCharacters(characters);
            getView().setLoading(false);
        });
    }

    public void onCharacterSelected(final long charID) {
        final EveAccount account = useCase.loadAccount(charID);
        if (null == account) {
            return;
        }
        Application.runWith(getContext(), account);//FIXME this is likely not going to work well

        final Intent intent = new Intent(getContext(), CharacterActivity.class);
        startActivity(intent);
    }
}
