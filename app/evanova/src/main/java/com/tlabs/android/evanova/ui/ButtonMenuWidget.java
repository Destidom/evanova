package com.tlabs.android.evanova.ui;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.character.CharacterWidget;
import com.tlabs.android.jeeves.views.corporation.CorporationWidget;
import com.tlabs.eve.api.EveAPI;

public class ButtonMenuWidget extends FrameLayout implements CharacterWidget, CorporationWidget {

    public interface Listener {

        void onButtonClicked(final int id);
    }

    private Listener listener;

    public ButtonMenuWidget(Context context) {
        super(context);
        init();
    }

    public ButtonMenuWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonMenuWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setCharacter(EveCharacter c) {
        setButtonVisible(R.id.menuCorporationButton, false);
        setButtonVisible(R.id.menuStarbasesButton, false);

        setButtonEnabled(
                R.id.menuSkillsButton,
                (null != c) && c.hasAllAccess(
                        EveAPI.CharacterAccess.CharacterSheet));

        setButtonEnabled(
                R.id.menuAssetsButton,
                (null != c) && c.hasAllAccess(EveAPI.CharacterAccess.AssetList));

        setButtonEnabled(
                R.id.menuMarketButton,
                (null != c) && c.hasAllAccess(EveAPI.CharacterAccess.MarketOrders));

        setButtonEnabled(
                R.id.menuWalletButton,
                (null != c) && c.hasAnyAccess(
                        EveAPI.CharacterAccess.WalletTransactions,
                        EveAPI.CharacterAccess.WalletJournal));

        setButtonEnabled(
                R.id.menuMailButton,
                (null != c) && c.hasAnyAccess(
                        EveAPI.CharacterAccess.MailMessages,
                        EveAPI.CharacterAccess.MailBodies,
                        EveAPI.CharacterAccess.MailingLists,
                        EveAPI.CharacterAccess.Notifications,
                        EveAPI.CharacterAccess.NotificationTexts));

        setButtonEnabled(
                R.id.menuSocialButton,
                (null != c) && c.hasAnyAccess(
                        EveAPI.CharacterAccess.ContactList,
                        EveAPI.CharacterAccess.Standings,
                        EveAPI.CharacterAccess.Bookmarks));

        setButtonEnabled(R.id.menuContractsButton,
                (null != c) && c.hasAnyAccess(EveAPI.CharacterAccess.Contracts));

        setButtonEnabled(R.id.menuIndustryJobsButton,
                (null != c) && c.hasAnyAccess(EveAPI.CharacterAccess.IndustryJobs));

        setButtonEnabled(R.id.menuCalendarButton,
                (null != c) && c.hasAnyAccess(EveAPI.CharacterAccess.UpcomingCalendarEvents));
    }

    @Override
    public void setCorporation(EveCorporation c) {
        setButtonVisible(R.id.menuCorporationButton, true);
        setButtonVisible(R.id.menuStarbasesButton, true);

        setButtonVisible(R.id.menuCalendarButton, false);
        setButtonVisible(R.id.menuMailButton, false);
        setButtonVisible(R.id.menuSkillsButton, false);

        setButtonEnabled(R.id.menuCorporationButton, true);

        setButtonEnabled(
                R.id.menuStarbasesButton,
                (null != c) && c.hasAnyAccess(
                        EveAPI.CorporationAccess.StarbaseList,
                        EveAPI.CorporationAccess.OutpostList));

        setButtonEnabled(
                R.id.menuAssetsButton,
                (null != c) && c.hasAllAccess(EveAPI.CorporationAccess.AssetList));

        setButtonEnabled(
                R.id.menuMarketButton,
                (null != c) && c.hasAllAccess(EveAPI.CorporationAccess.MarketOrders));

        setButtonEnabled(
                R.id.menuWalletButton,
                (null != c) && c.hasAnyAccess(
                        EveAPI.CorporationAccess.WalletTransactions,
                        EveAPI.CorporationAccess.WalletJournal));

        setButtonEnabled(
                R.id.menuContractsButton,
                (null != c) && c.hasAnyAccess(EveAPI.CorporationAccess.Contracts));

        setButtonEnabled(
                R.id.menuIndustryJobsButton,
                (null != c) && c.hasAnyAccess(EveAPI.CorporationAccess.IndustryJobs));

        setButtonEnabled
                (R.id.menuSocialButton,
                (null != c) && c.hasAnyAccess(
                    EveAPI.CorporationAccess.MemberTrackingLimited,
                    EveAPI.CorporationAccess.MemberTrackingExtended,
                    EveAPI.CorporationAccess.ContactList,
                    EveAPI.CorporationAccess.Standings,
                    EveAPI.CorporationAccess.Bookmarks));
    }


    public final void setButtonEnabled(int buttonId, boolean enabled) {
        final Button button = (Button)findViewById(buttonId);
        if (null != button) {
            button.setEnabled(enabled);
            button.setVisibility(VISIBLE);
            LevelListDrawable topLevellist = (LevelListDrawable)button.getCompoundDrawables()[1];//top
            if (null == topLevellist) {
                LevelListDrawable backgroundList = (LevelListDrawable)button.getBackground();
                backgroundList.setLevel((enabled) ? 1 : 0);
            }
            else {
                topLevellist.setLevel((enabled) ? 1 : 0);
            }
        }
    }

    public final void setButtonVisible(int buttonId, boolean visible) {
        final Button button = (Button)findViewById(buttonId);
        if (null != button) {
            button.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.menu_buttons, this);
        install(this);
    }

    private void install(final ViewGroup view) {
        final int children = view.getChildCount();
        for (int i = 0; i < children; i++) {
            final View kid = view.getChildAt(i);
            if (kid instanceof Button) {
                kid.setOnClickListener(b -> {
                    if (null != listener) {
                        listener.onButtonClicked(b.getId());
                    }
                });
            }
            else if (kid instanceof ViewGroup) {
                install((ViewGroup)kid);
            }
        }
    }

    private void setEnabled(final int buttonID, final boolean enabled) {
        final Button button = (Button)findViewById(buttonID);
        if (null == button) {
            return;
        }

    }
}
