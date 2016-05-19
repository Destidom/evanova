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

public class DashboardUseCaseImpl implements DashboardUseCase {

    private ContentFacade content;
    private CrestService crest;
    private EveNetwork eve;

    @Inject
    public DashboardUseCaseImpl(ContentFacade content, CrestService crest, EveNetwork eve) {
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
        return crest.getServerStatus();
    }

    @Override
    public List<EveRSSEntry> loadRSS() {
        return eve.execute(new EveRSSRequest()).getEntries();
    }
}
