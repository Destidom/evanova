package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.corporation.StarbaseDetailsRequest;
import com.tlabs.eve.api.corporation.StarbaseDetailsResponse;

public final class CorporationStarbaseAction extends EveSingleAction<StarbaseDetailsRequest, StarbaseDetailsResponse> {
    public CorporationStarbaseAction(final Context context, final long corpID, final long starbaseID) {
        super(context, new StarbaseDetailsRequest(Long.toString(corpID), Long.toString(starbaseID)));
    }
}
