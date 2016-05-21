package com.tlabs.android.evanova.app.dashboard.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterListActivity;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;
import com.tlabs.android.evanova.app.corporation.ui.CorporationListActivity;
import com.tlabs.android.evanova.app.dashboard.DashboardUseCase;
import com.tlabs.android.evanova.app.dashboard.DashboardView;
import com.tlabs.android.evanova.app.fitting.ui.ShipFittingListActivity;
import com.tlabs.android.evanova.app.items.ui.ItemDatabaseActivity;
import com.tlabs.android.evanova.app.route.ui.RouteActivity;
import com.tlabs.android.evanova.app.skills.SkillDatabaseActivity;
import com.tlabs.android.jeeves.model.EveAccount;

import java.util.List;

import javax.inject.Inject;

public class DashboardPresenter extends EvanovaActivityPresenter<DashboardView> {

    private final DashboardUseCase useCase;

    @Inject
    public DashboardPresenter(Context context, DashboardUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(DashboardView view) {
        super.setView(view);
        setBackgroundDefault();

        setTitle(R.string.app_name);
        setTitleDescription("");
        loadAccounts();
    }

    public void loadAccounts() {
        showLoading(true);
        subscribe(() -> this.useCase.loadAccounts(),
                accounts -> {
                    showLoading(false);
                    getView().setAccounts(accounts);
                    loadLastAccount(accounts);
                });
    }

    public boolean onDrawerAccountSelected(final EveAccount account, final boolean current) {
        if (!current) {
            Application.runWith(getContext(), account);
            return true;
        }

        if (null == account) {
            return false;
        }

        Intent intent;
        switch (account.getType()) {
            case EveAccount.ACCOUNT:
            case EveAccount.CHARACTER:
                intent = new Intent(getContext(), CharacterActivity.class);
                intent.putExtra(CharacterActivity.EXTRA_CHAR_ID, account.getOwnerId());
                break;
            case EveAccount.CORPORATION:
                intent = new Intent(getContext(), CorporationActivity.class);
                intent.putExtra(CorporationActivity.EXTRA_CORP_ID, account.getOwnerId());
                break;
            default:
                intent = new Intent(getContext(), AccountActivity.class);
                break;
        }
        savedPreferences().setLastViewedAccount(account.getId());
        return startActivity(intent);
    }

    public boolean onDrawerMenuSelected(final int itemID, final boolean longClick) {
        Intent intent = null;
        switch (itemID) {
            case R.id.menu_drawer_account:
                intent = new Intent(getContext(), AccountActivity.class);
                break;
            case R.id.menu_drawer_characters:
                intent = new Intent(getContext(), CharacterListActivity.class);
                break;
            case R.id.menu_drawer_corporations:
                intent = new Intent(getContext(), CorporationListActivity.class);
                break;

            case R.id.menu_drawer_skills:
                intent = new Intent(getContext(), SkillDatabaseActivity.class);
                break;
            case R.id.menu_drawer_items:
                intent = new Intent(getContext(), ItemDatabaseActivity.class);
                break;
            case R.id.menu_drawer_routes:
                intent = new Intent(getContext(), RouteActivity.class);
                break;
            case R.id.menu_drawer_fittings:
                intent = new Intent(getContext(), ShipFittingListActivity.class);
                break;
            case R.id.menu_drawer_settings:
                getView().showSettings();
                break;
            case R.id.menu_drawer_server:
                getView().showServerStatus();
                getView().setLoading(true);
                subscribe(() -> useCase.loadServerStatus(), status -> getView().updateServerStatus(status));
                subscribe(() -> useCase.loadRSS(), rss -> {
                    getView().updateRSS(rss);
                    getView().setLoading(false);
                });
                break;
            case R.id.menu_drawer_help:
                getView().showAbout();
                break;
            default:
                break;
        }
        return startActivity(intent);
    }


    private void loadLastAccount(final List<EveAccount> accounts) {
        if (accounts.size() == 0) {
            getView().selectAccount(-1);
            return;
        }

        final long last = savedPreferences().getLastViewedAccount();
        if (last == -1) {
            final EveAccount account = accounts.get(0);
            Application.runWith(getContext(), account);
            getView().selectAccount(account.getId());
            return;
        }

        for (EveAccount a: accounts) {
            if (a.getId() == last) {
                Application.runWith(getContext(), a);
                getView().selectAccount(a.getId());
                return;
            }
        }
        getView().selectAccount(-1);
    }
}
