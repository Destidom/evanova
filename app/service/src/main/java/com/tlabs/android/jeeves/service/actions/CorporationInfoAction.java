package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.AccountBalanceResponse;
import com.tlabs.eve.api.AssetListResponse;
import com.tlabs.eve.api.corporation.CorporationAccountBalanceRequest;
import com.tlabs.eve.api.corporation.CorporationAssetsRequest;
import com.tlabs.eve.api.corporation.CorporationSheetRequest;
import com.tlabs.eve.api.corporation.CorporationSheetResponse;
import com.tlabs.eve.api.corporation.MemberTrackingRequest;

public final class CorporationInfoAction extends EveAction {

    public CorporationInfoAction(final Context context, final long corpID) {
        super(
                context,
                new CorporationSheetRequest(Long.toString(corpID)),
                new CorporationAssetsRequest(Long.toString(corpID)),
                new CorporationAccountBalanceRequest(Long.toString(corpID)),
                new MemberTrackingRequest(Long.toString(corpID), false));
    }
    
    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response.getCached()) {
            return null;
        }
        if (request instanceof CorporationSheetRequest) {
            onActionCompleted((CorporationSheetRequest)request, (CorporationSheetResponse)response);
            return null;
        }
        if (request instanceof CorporationAccountBalanceRequest) {
            onActionCompleted((CorporationAccountBalanceRequest)request, (AccountBalanceResponse)response);
            return null;
        }
        if (request instanceof CorporationAssetsRequest) {
            onActionCompleted((CorporationAssetsRequest)request, (AssetListResponse)response);
            return null;
        }

        return null;
    }
    
    private void onActionCompleted(final CorporationSheetRequest request, final CorporationSheetResponse response) {
        response.getCorporationInfo().setCorporationID(Long.parseLong(request.getCorporationID()));
        evanova.updateCorporation(response.getCorporationInfo());
    }
    
    private void onActionCompleted(final CorporationAccountBalanceRequest request, final AccountBalanceResponse response) {
        evanova.updateCorporationBalance(Long.parseLong(request.getCorporationID()), response.getAccountBalance());
    }

    private void onActionCompleted(CorporationAssetsRequest request, AssetListResponse response) {
        if (response.getAssets().size() == 0) {
            return;
        }
        evanova.saveAssets(
                Long.parseLong(request.getCorporationID()),
                response.getAssets());
    }

}
