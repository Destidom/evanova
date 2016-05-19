package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tlabs.android.evanova.app.character.CharacterComponent;
import com.tlabs.android.evanova.app.character.presenter.CharacterPresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterInfoWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;

import javax.inject.Inject;

public class CharacterViewFragment extends CharacterFragment {

    private CharacterInfoWidget wInfo;
    private CharacterTrainingDetailsWidget wTraining;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        this.wInfo = new CharacterInfoWidget(getContext());
        this.wInfo.setListener(() -> presenter.onCharacterDetailsSelected());
        view.addView(this.wInfo);

        this.wTraining = new CharacterTrainingDetailsWidget(getContext());
        this.wTraining.setListener(() -> presenter.onCharacterTrainingSelected());
        view.addView(this.wTraining);

        return view;
    }

    @Override
    protected void onCharacterChanged(EveCharacter character) {
        wInfo.setCharacter(character);
        wTraining.setTraining(character.getTraining());
    }

}
