package com.tlabs.android.evanova.app.character.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.character.CharacterUseCase;
import com.tlabs.android.evanova.app.character.CharacterView;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;

import javax.inject.Inject;

public class CharacterPresenter extends EvanovaActivityPresenter<CharacterView> {

    private final CharacterUseCase useCase;

    private EveCharacter character = null;

    @Inject
    public CharacterPresenter(
            Context context,
            CharacterUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    public void startWithIntent(final Intent intent) {
        final long id = (null == intent) ? -1l : intent.getLongExtra(CharacterActivity.EXTRA_CHAR_ID, -1l);
        if (id == -1l) {
            return;
        }
        getView().setLoading(true);
        subscribe(
                () -> this.useCase.loadCharacter(id),
                character -> {
                    getView().setLoading(false);
                    getView().showMainView(character);
                    setCharacter(character);
                });
    }

    public void onCharacterDetailsSelected() {
        setBackground((EveCharacter)null);
        getView().showDetails(this.character);
    }

    public void onCharacterTrainingSelected() {
        setBackground((EveCharacter)null);
        getView().showTraining(this.character);
    }

    private void setCharacter(final EveCharacter character) {
        this.character = character;
        if (null == character) {
            setTitle(R.string.app_name);
            setTitleDescription("");
            setTitleIcon(R.drawable.ic_eve_member);
        }
        else {
            setTitle(character.getName());
            setTitleDescription(character.getCorporationName());
            setTitleIcon(EveImages.getCharacterIconURL(getContext(), character.getID()));
        }
        setBackground(character);
    }
}
