package com.tlabs.android.evanova.app.character.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.character.CharacterDetailsView;
import com.tlabs.android.evanova.app.character.CharacterMailView;
import com.tlabs.android.evanova.app.character.CharacterSkillView;
import com.tlabs.android.evanova.app.character.CharacterTrainingView;
import com.tlabs.android.evanova.app.character.CharacterUseCase;
import com.tlabs.android.evanova.app.character.CharacterView;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;

import javax.inject.Inject;

public class CharacterPresenter extends EvanovaActivityPresenter<CharacterView> {

    private final CharacterUseCase useCase;

    private final CharacterMailPresenter mailPresenter;
    private final CharacterSkillsPresenter skillsPresenter;

    private EveCharacter character = null;

    @Inject
    public CharacterPresenter(
            Context context,
            CharacterUseCase useCase,
            CharacterMailPresenter mailPresenter,
            CharacterSkillsPresenter skillsPresenter) {
        super(context);

        this.useCase = useCase;
        this.mailPresenter = mailPresenter;
        this.skillsPresenter = skillsPresenter;
    }

    public void setView(final CharacterMailView view) {
        this.mailPresenter.setView(view);
    }

    public void setView(final CharacterSkillView view) {
        this.skillsPresenter.setView(view);
    }

    public void setView(final CharacterTrainingView view) {
        view.showTraining(this.character.getTraining());
    }

    public void setView(final CharacterDetailsView view) {
        view.showCharacterDetails(this.character);
    }

    @Override
    public void destroyView() {
        super.destroyView();
        this.mailPresenter.destroyView();
        this.skillsPresenter.destroyView();
    }

    @Override
    public void startView(final Intent intent) {
        final long id = (null == intent) ? -1l : intent.getLongExtra(CharacterActivity.EXTRA_CHAR_ID, -1l);
        if (id == -1l) {
            return;
        }
        getView().showLoading(true);
        subscribe(
                () -> {
                    final EveCharacter loaded = this.useCase.loadCharacter(id);
                    this.character = loaded;
                    this.mailPresenter.setCharacter(loaded);
                    this.skillsPresenter.setCharacter(loaded);
                    return loaded;
                },
                character -> {
                    getView().showMainView(character);
                    getView().showLoading(false);
                    showCharacterImpl(character);
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

    public void onCharacterMenuSelected(final int buttonID) {
        switch(buttonID) {
            case R.id.menuSkillsButton:
                getView().showSkills(this.character);
                break;
            case R.id.menuMailButton:
                getView().showMails(this.character);
                break;
            case R.id.menuAssetsButton:
                getView().showAssets(this.character);
                break;
            case R.id.menuWalletButton:
                getView().showWallet(this.character);
                break;
            case R.id.menuMarketButton:
                getView().showMarketOrders(this.character);
                break;
            case R.id.menuContractsButton:
                getView().showContracts(this.character);
                break;
            case R.id.menuIndustryJobsButton:
                getView().showIndustry(this.character);
                break;
            case R.id.menuSocialButton:
                getView().showSocial(this.character);
                break;
            case R.id.menuCalendarButton:
                getView().showCalendar(this.character);
                break;
            default:
                break;
        }
    }

    private void showCharacterImpl(final EveCharacter character) {
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
