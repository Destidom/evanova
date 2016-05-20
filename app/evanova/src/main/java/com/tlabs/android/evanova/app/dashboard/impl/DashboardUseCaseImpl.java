package com.tlabs.android.evanova.app.dashboard.impl;

import com.tlabs.android.evanova.app.dashboard.DashboardUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.ccp.EveRSSEntry;
import com.tlabs.eve.ccp.EveRSSRequest;

import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestServerStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class DashboardUseCaseImpl implements DashboardUseCase {

    private ContentFacade content;
    private Lazy<CrestService> crest;
    private Lazy<EveNetwork> eve;

    @Inject
    public DashboardUseCaseImpl(ContentFacade content, Lazy<CrestService> crest, Lazy<EveNetwork> eve) {
        this.content = content;
        this.crest = crest;
        this.eve = eve;
    }

    @Override
    public List<EveAccount> loadAccounts() {
        final List<Long> ids = content.listAccounts();
        final List<EveAccount> accounts = new ArrayList<>(ids.size());
        for (Long id: ids) {
            accounts.add(content.getAccount(id));
        }
        return accounts;
    }

    @Override
    public CrestServerStatus loadServerStatus() {
        return crest.get().getServerStatus();
    }

    @Override
    public List<EveRSSEntry> loadRSS() {
        return eve.get().execute(new EveRSSRequest()).getEntries();
    }
}
