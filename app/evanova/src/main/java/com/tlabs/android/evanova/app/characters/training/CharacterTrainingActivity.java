package com.tlabs.android.evanova.app.characters.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingListWidget;

import javax.inject.Inject;

public class CharacterTrainingActivity extends BaseActivity implements CharacterTrainingView {

    @Inject
    @Presenter
    CharacterTrainingPresenter presenter;

    private CharacterTrainingDetailsWidget wDetails;
    private CharacterTrainingListWidget wList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerCharacterComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);

        final View view = LayoutInflater.from(this).inflate(R.layout.f_character_training, null);
        this.wDetails = (CharacterTrainingDetailsWidget)view.findViewById(R.id.f_character_training_TrainingDetailsWidget);
        this.wList = (CharacterTrainingListWidget)view.findViewById(R.id.f_character_training_TrainingListWidget);

        this.setView(view);
    }

    @Override
    public void setCharacter(final EveCharacter character) {
        this.wDetails.setCharacter(character);
        this.wList.setCharacter(character);
    }
}
