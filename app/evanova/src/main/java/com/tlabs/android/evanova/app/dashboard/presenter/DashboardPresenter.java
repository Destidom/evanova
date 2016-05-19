package com.tlabs.android.evanova.app.dashboard.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterListActivity;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;
import com.tlabs.android.evanova.app.dashboard.DashboardUseCase;
import com.tlabs.android.evanova.app.dashboard.DashboardView;
import com.tlabs.android.evanova.app.fitting.ui.ShipFittingListActivity;
import com.tlabs.android.evanova.app.route.ui.RouteInputActivity;
import com.tlabs.android.evanova.mvp.ActivityPresenter;
import com.tlabs.android.jeeves.model.EveAccount;

import javax.inject.Inject;

public class DashboardPresenter extends ActivityPresenter<DashboardView> {

    private final DashboardUseCase useCase;

    @Inject
    public DashboardPresenter(Context context, DashboardUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(DashboardView view) {
        super.setView(view);
        setTitle(R.string.app_name);
        setTitleDescription("");
        loadAccounts();
    }

    public void loadAccounts() {
        setLoading(true);
        subscribe(() -> this.useCase.loadAccounts(),
                accounts -> {
                    setLoading(false);
                    getView().setAccounts(accounts);
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
                break;
            case EveAccount.CORPORATION:
                intent = new Intent(getContext(), CorporationActivity.class);
                break;
            default:
                intent = new Intent(getContext(), AccountActivity.class);
                break;
        }
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
            //    intent = new Intent(getContext(), CorporationListActivity.class);
                break;

           /* case R.id.menu_drawer_skills:
                intent = new Intent(getContext(), SkillListActivity.class);
                break;
            case R.id.menu_drawer_items:
                intent = new Intent(getContext(), ItemDatabaseActivity.class);
                break;*/
            case R.id.menu_drawer_routes:
                intent = new Intent(getContext(), RouteInputActivity.class);
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


}
