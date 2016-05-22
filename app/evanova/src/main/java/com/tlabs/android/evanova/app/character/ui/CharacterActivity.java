package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.character.CharacterModule;
import com.tlabs.android.evanova.app.character.CharacterView;
import com.tlabs.android.evanova.app.character.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.character.presenter.CharacterPresenter;
import com.tlabs.android.evanova.app.character.ui.assets.CharacterAssetsFragment;
import com.tlabs.android.evanova.app.character.ui.calendar.CharacterCalendarFragment;
import com.tlabs.android.evanova.app.character.ui.contracts.CharacterContractsFragment;
import com.tlabs.android.evanova.app.character.ui.industry.CharacterIndustryFragment;
import com.tlabs.android.evanova.app.character.ui.mails.CharacterMailboxListFragment;
import com.tlabs.android.evanova.app.character.ui.orders.CharacterMarketFragment;
import com.tlabs.android.evanova.app.character.ui.skills.CharacterSkillsFragment;
import com.tlabs.android.evanova.app.character.ui.social.CharacterSocialFragment;
import com.tlabs.android.evanova.app.character.ui.wallet.CharacterWalletFragment;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;

import javax.inject.Inject;

public class CharacterActivity extends BaseActivity implements CharacterView {

    public static final String EXTRA_CHAR_ID = CharacterActivity.class.getSimpleName() + ".charID";

    @Inject
    @Presenter
    CharacterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCharacterComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);
    }

    @Override
    public void showMainView(EveCharacter character) {
        showCharacterFragment(new CharacterViewFragment(), character, false);
    }

    @Override
    public void showDetails(EveCharacter character) {
        showCharacterFragment(new CharacterDetailsFragment(), character, true);
    }

    @Override
    public void showTraining(EveCharacter character) {
        showCharacterFragment(new CharacterTrainingFragment(), character, true);
    }

    @Override
    public void showSkills(EveCharacter character) {
        showCharacterFragment(new CharacterSkillsFragment(), character, true);
    }

    @Override
    public void showMails(EveCharacter character) {
        showCharacterFragment(new CharacterMailboxListFragment(), character, true);
    }

    @Override
    public void showAssets(EveCharacter character) {
        showCharacterFragment(new CharacterAssetsFragment(), character, true);
    }

    @Override
    public void showWallet(EveCharacter character) {
        showCharacterFragment(new CharacterWalletFragment(), character, true);
    }

    @Override
    public void showMarketOrders(EveCharacter character) {
        showCharacterFragment(new CharacterMarketFragment(), character, true);
    }

    @Override
    public void showContracts(EveCharacter character) {
        showCharacterFragment(new CharacterContractsFragment(), character, true);
    }

    @Override
    public void showIndustry(EveCharacter character) {
        showCharacterFragment(new CharacterIndustryFragment(), character, true);
    }

    @Override
    public void showSocial(EveCharacter character) {
        showCharacterFragment(new CharacterSocialFragment(), character, true);
    }

    @Override
    public void showCalendar(EveCharacter character) {
        showCharacterFragment(new CharacterCalendarFragment(), character, true);
    }

    private <T extends CharacterFragment> void showCharacterFragment(final T f, final EveCharacter character, final boolean stack) {
        f.setPresenter(this.presenter);
        if (stack) {
            stackFragment(f);
        }
        else {
            setFragment(f);
        }
        f.setCharacter(character);
    }
}
