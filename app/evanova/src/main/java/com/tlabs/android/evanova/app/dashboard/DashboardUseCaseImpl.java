package com.tlabs.android.evanova.app.dashboard;

import android.content.Context;

import com.tlabs.android.evanova.app.dashboard.DashboardUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.network.EveCrest;
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
    private Lazy<EveNetwork> eve;

    private final Context context;

    @Inject
    public DashboardUseCaseImpl(
            Context context,
            ContentFacade content,
            Lazy<EveNetwork> eve) {
        this.context = context;
        this.content = content;
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
        final CrestService crest = EveCrest.obtainService(context);
        if (null != crest) {
            return crest.getServerStatus();
        }
        return null;
    }

    @Override
    public List<EveRSSEntry> loadRSS() {
        return eve.get().execute(new EveRSSRequest()).getEntries();
    }
}
