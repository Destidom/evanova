package com.tlabs.android.evanova.app.characters.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.characters.CharacterUseCase;
import com.tlabs.android.evanova.app.characters.calendar.CharacterCalendarActivity;
import com.tlabs.android.evanova.app.contracts.main.ContractActivity;
import com.tlabs.android.evanova.app.mails.main.MailActivity;
import com.tlabs.android.evanova.app.market.main.MarketOrdersActivity;
import com.tlabs.android.evanova.app.skills.main.SkillDatabaseActivity;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

public class CharacterPresenter extends EvanovaActivityPresenter<CharacterMainView> {
    private static final Logger LOG = LoggerFactory.getLogger(CharacterPresenter.class);

    private final CharacterUseCase useCase;

    private EveCharacter character = null;
    private Subscription subscription = null;

    @Inject
    public CharacterPresenter(
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

    @Override
    public void startView(Intent intent) {
        final long charID = ownerOf(intent);
        if (charID <= 0) {
            return;
        }
        subscribe(
                () -> {
                    final EveCharacter loaded = this.useCase.loadCharacter(charID);
                    this.character = loaded;
                    return loaded;
                },
                character -> {
                    getView().setCharacter(character);
                    showCharacterImpl(character);
                    subscribe(character);
                });
    }

    public void onCharacterDetailsSelected() {
        setBackground((EveCharacter)null);
        getView().showCharacterDetails();
    }

    public void onCharacterTrainingSelected() {

    }

    public void onCharacterMenuSelected(final int buttonID) {
        if (null == this.character) {
            return;
        }

        switch(buttonID) {
            case R.id.menuSkillsButton: {
                final Intent intent = new Intent(getContext(), SkillDatabaseActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
                break;
            }
            case R.id.menuMailButton: {
                final Intent intent = new Intent(getContext(), MailActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
                break;
            }
            case R.id.menuAssetsButton:

                break;
            case R.id.menuWalletButton:

                break;
            case R.id.menuMarketButton: {
                final Intent intent = new Intent(getContext(), MarketOrdersActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
                break;
            }
            case R.id.menuContractsButton: {
                final Intent intent = new Intent(getContext(), ContractActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
                break;
            }
            case R.id.menuIndustryJobsButton:

                break;
            case R.id.menuSocialButton:

                break;

            case R.id.menuCalendarButton: {
                final Intent intent = new Intent(getContext(), CharacterCalendarActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.character.getID());
                startActivity(intent);
                break;
            }
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
