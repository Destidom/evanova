package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingListWidget;

public class CharacterTrainingFragment extends CharacterFragment {

    private CharacterTrainingDetailsWidget wDetails;
    private CharacterTrainingListWidget wTraining;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        this.wDetails = new CharacterTrainingDetailsWidget(getContext());
        //this.wDetails.setListener(() -> presenter.onCharacterDetailsSelected());
        view.addView(this.wDetails);

        this.wTraining = new CharacterTrainingListWidget(getContext());
      //  this.wTraining.setListener(() -> presenter.onCharacterTrainingSelected());
        view.addView(this.wTraining);

        return view;
    }

    @Override
    protected void onCharacterChanged(EveCharacter character) {
        wDetails.setTraining(character.getTraining());
        wTraining.setTraining(character.getTraining());
    }

}
