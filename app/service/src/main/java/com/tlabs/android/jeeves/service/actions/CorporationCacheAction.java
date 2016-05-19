package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.api.corporation.CorporationIndustryJobsRequest;
import com.tlabs.eve.api.corporation.CorporationMarketOrderRequest;
import com.tlabs.eve.api.corporation.CorporationStandingsRequest;
import com.tlabs.eve.api.corporation.CorporationWalletJournalRequest;
import com.tlabs.eve.api.corporation.CorporationWalletTransactionsRequest;
import com.tlabs.eve.api.corporation.MemberTrackingRequest;
import com.tlabs.eve.api.corporation.OutpostListRequest;
import com.tlabs.eve.api.corporation.StarbaseListRequest;

import java.util.ArrayList;
import java.util.List;

public final class CorporationCacheAction extends EveAction {
    public CorporationCacheAction(final Context context, final long corpID) {
        super(context, false, buildRequests(corpID));
    }

    private static EveRequest<?>[] buildRequests(final long corpID) {
        final String sCorpID = Long.toString(corpID);
        final List<EveRequest<?>> allCached = new ArrayList<>();
        allCached.add(new MemberTrackingRequest(sCorpID, true));
        allCached.add(new CorporationMarketOrderRequest(sCorpID));
        allCached.add(new CorporationIndustryJobsRequest(sCorpID));
        allCached.add(new StarbaseListRequest(sCorpID));
        allCached.add(new OutpostListRequest(sCorpID));

        for (int i = 1000; i < 1007; i++) {
            allCached.add(new CorporationWalletTransactionsRequest(sCorpID, i));
            allCached.add(new CorporationWalletJournalRequest(sCorpID, i));
        }
        allCached.add(new CorporationStandingsRequest(sCorpID));
        return allCached.toArray(new EveRequest<?>[allCached.size()]);
    }
}
