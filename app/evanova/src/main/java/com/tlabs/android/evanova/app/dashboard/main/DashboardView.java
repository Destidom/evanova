package com.tlabs.android.evanova.app.dashboard.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.ccp.EveRSSEntry;

import org.devfleet.crest.model.CrestServerStatus;

import java.util.List;

public interface DashboardView extends ActivityView {

    void selectAccount(final long accountID);

    void setAccounts(final List<EveAccount> accounts);

    void showAbout();

    void showServerStatus();

    void showSettings();

    void updateServerStatus(final CrestServerStatus status);

    void updateRSS(final List<EveRSSEntry> rss);
}
