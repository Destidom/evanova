package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.ContractItemsResponse;
import com.tlabs.eve.api.corporation.CorporationContractItemsRequest;

public final class CorporationContractItemsAction extends EveSingleAction<CorporationContractItemsRequest, ContractItemsResponse> {

    public CorporationContractItemsAction(final Context context, final long corpID, final long contractID) {
        super(context, new CorporationContractItemsRequest(Long.toString(corpID), contractID));
    }

    @Override
    protected EveAction onActionCompleted(final CorporationContractItemsRequest request, final ContractItemsResponse response) {
        if (response.getCached()) {
            return null;
        }
        evanova.updateContract(
                Long.parseLong(request.getCorporationID()),
                request.getContractID(),
                response.getItems());
        return null;
    }
}
