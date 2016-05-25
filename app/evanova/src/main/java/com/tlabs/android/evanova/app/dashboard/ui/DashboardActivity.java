package com.tlabs.android.evanova.app.dashboard.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.dashboard.DaggerDashboardComponent;
import com.tlabs.android.evanova.app.dashboard.DashboardComponent;
import com.tlabs.android.evanova.app.dashboard.DashboardModule;
import com.tlabs.android.evanova.app.dashboard.DashboardView;
import com.tlabs.android.evanova.app.dashboard.presenter.DashboardPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.ccp.EveRSSEntry;

import org.devfleet.crest.model.CrestServerStatus;

import java.util.List;

import javax.inject.Inject;

public final class DashboardActivity extends BaseActivity implements DashboardView {

    @Inject
    @Presenter
    DashboardPresenter presenter;

    private DrawerSupport drawer;

    private AboutFragment fAbout;
    private ServerStatusFragment fStatus;
    private PreferencesFragment fPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.drawer = new DrawerSupport(this, savedInstanceState) {
            @Override
            protected boolean onDrawerMenuSelected(int item, boolean selected) {
                return presenter.onDrawerMenuSelected(item, selected);
            }

            @Override
            protected boolean onDrawerAccountSelected(EveAccount selected, boolean current) {
                return presenter.onDrawerAccountSelected(selected, current);
            }
        };

        this.fAbout = new AboutFragment();
        this.fStatus = new ServerStatusFragment();

        this.fPreferences = new PreferencesFragment();
        final DashboardComponent component =
                DaggerDashboardComponent
                .builder()
                .dashboardModule(new DashboardModule())
                .applicationComponent(Application.getAppComponent())
                .build();

        component.inject(this);
        component.inject(this.fPreferences);
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpened()) {
            finish();
        }
        else {
            this.drawer.openDrawer();
        }
    }

    @Override
    public void selectAccount(long accountID) {
        this.drawer.setSelected(accountID);
    }

    @Override
    public void setAccounts(List<EveAccount> accounts) {
        this.drawer.setAccounts(accounts);
    }

    @Override
    public void showAbout() {
        stackFragment(this.fAbout);
        this.drawer.closeDrawer();
    }

    @Override
    public void showServerStatus() {
        stackFragment(this.fStatus);
        this.drawer.closeDrawer();
    }

    @Override
    public void showSettings() {
        stackFragment(this.fPreferences);
        this.drawer.closeDrawer();
    }

    @Override
    public void updateServerStatus(CrestServerStatus status) {
        this.fStatus.setServerStatus(status);
    }

    @Override
    public void updateRSS(List<EveRSSEntry> rss) {
        this.fStatus.setRSS(rss);
    }
}