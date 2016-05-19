package com.tlabs.android.evanova.app.dashboard;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.ccp.EveRSSEntry;

import org.devfleet.crest.model.CrestServerStatus;

import java.util.List;

public interface DashboardUseCase {

    List<EveAccount> loadAccounts();

    CrestServerStatus loadServerStatus();

    List<EveRSSEntry> loadRSS();

}
