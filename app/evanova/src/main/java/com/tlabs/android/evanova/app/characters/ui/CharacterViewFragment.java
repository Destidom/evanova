package com.tlabs.android.evanova.app.characters.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.characters.CharacterFragment;
import com.tlabs.android.evanova.app.characters.CharacterActivityView;
import com.tlabs.android.evanova.app.characters.presenter.CharacterMainViewPresenter;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.evanova.ui.ButtonMenuWidget;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterInfoWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;

import javax.inject.Inject;

import butterknife.BindView;

public class CharacterViewFragment extends CharacterFragment implements CharacterActivityView {

    public static CharacterViewFragment newInstance(final long charID) {
        Bundle bundle = new Bundle();
        bundle.putLong(CharacterViewActivity.EXTRA_CHAR_ID, charID);

        final CharacterViewFragment f = new CharacterViewFragment();
        f.setArguments(bundle);
        return f;
    }

    @Inject
    @Presenter
    CharacterMainViewPresenter presenter;

    @BindView(R.id.f_character_CharacterInfoWidget)
    CharacterInfoWidget wInfo;

    @BindView(R.id.f_character_CharacterTrainingDetailsWidget)
    CharacterTrainingDetailsWidget wTraining;

    @BindView(R.id.f_character_ButtonMenuWidget)
    ButtonMenuWidget wMenu;

    @Override
    public void onStart() {
        super.onStart();
        this.presenter.setCharacter(
                getArguments().getLong(CharacterViewActivity.EXTRA_CHAR_ID, -1l));
    }

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_character_view, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.wInfo.setListener(() -> presenter.onCharacterDetailsSelected());
        this.wTraining.setListener(() -> presenter.onCharacterTrainingSelected());
        this.wMenu.setListener(buttonId -> presenter.onCharacterMenuSelected(buttonId));
    }

    @Override
    public void showCharacter(EveCharacter character) {
        wMenu.setCharacter(character);
        wInfo.setCharacter(character);
        wTraining.setTraining(character.getTraining());
    }

    @Override
    public void updateCharacter(EveCharacter character) {
        wInfo.setCharacter(character);
    }

    @Override
    public void showMainView(EveCharacter character) {

    }

    @Override
    public void showDetails(EveCharacter character) {

    }

    @Override
    public void showTraining(EveCharacter character) {

    }

    @Override
    public void showSkills(EveCharacter character) {

    }

    @Override
    public void showMails(EveCharacter character) {

    }

    @Override
    public void showAssets(EveCharacter character) {

    }

    @Override
    public void showWallet(EveCharacter character) {

    }

    @Override
    public void showMarketOrders(EveCharacter character) {

    }

    @Override
    public void showContracts(EveCharacter character) {

    }

    @Override
    public void showIndustry(EveCharacter character) {

    }

    @Override
    public void showSocial(EveCharacter character) {

    }

    @Override
    public void showCalendar(EveCharacter character) {

    }
}
