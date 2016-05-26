package com.tlabs.android.evanova.app.characters.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.characters.CharacterUseCase;
import com.tlabs.android.evanova.app.characters.CharacterActivityView;
import com.tlabs.android.evanova.app.mails.ui.MailActivity;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

public class CharacterMainViewPresenter extends EvanovaActivityPresenter<CharacterActivityView> {
    private static final Logger LOG = LoggerFactory.getLogger(CharacterMainViewPresenter.class);

    private final CharacterUseCase useCase;

    private EveCharacter character = null;
    private Subscription subscription = null;

    @Inject
    public CharacterMainViewPresenter(
            Context context,
            CharacterUseCase useCase) {
        super(context);

        this.useCase = useCase;
    }

    @Override
    public void destroyView() {
        if (null != this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }

        super.destroyView();
    }

    public void setCharacter(final long charID) {
        if (charID <= 0) {
            return;
        }
        showLoading(true);
        subscribe(
                () -> {
                    final EveCharacter loaded = this.useCase.loadCharacter(charID);
                    this.character = loaded;
                    return loaded;
                },
                character -> {
                    getView().showCharacter(character);
                    showLoading(false);
                    showCharacterImpl(character);
                    subscribe(character);
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
        if (null == this.character) {
            return;
        }

        switch(buttonID) {
            case R.id.menuSkillsButton:
                getView().showSkills(this.character);
                break;
            case R.id.menuMailButton:
                final Intent intent = new Intent(getContext(), MailActivity.class);
                intent.putExtra(MailActivity.EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
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

    private void subscribe(final EveCharacter character) {
        if (null != this.subscription) {
            this.subscription.unsubscribe();
        }
        if ((null == character) || !character.hasCrest()) {
            return;
        }
        if (!isNetworkAvailable()) {
            return;
        }

        this.subscription = this.useCase.subscribe(character, new Observer<EveCharacter>() {
            @Override
            public void onCompleted() {
                LOG.error("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LOG.error(e.getLocalizedMessage(), e);
            }

            @Override
            public void onNext(EveCharacter character) {
                if (null != getView()) {
                    getView().updateCharacter(character);
                }
            }
        });
    }
}
