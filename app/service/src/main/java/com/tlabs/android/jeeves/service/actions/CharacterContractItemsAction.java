package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.ContractItemsResponse;
import com.tlabs.eve.api.character.CharacterContractItemsRequest;

public final class CharacterContractItemsAction extends EveSingleAction<CharacterContractItemsRequest, ContractItemsResponse> {

    public CharacterContractItemsAction(final Context context, final long charID, final long contractID) {
        super(context, new CharacterContractItemsRequest(Long.toString(charID), contractID));
    }

    @Override
    protected EveAction onActionCompleted(final CharacterContractItemsRequest request, final ContractItemsResponse response) {
        if (response.getCached()) {
            return null;
        }
        evanova.updateContract(
                Long.parseLong(request.getCharacterID()),
                request.getContractID(),
                response.getItems());
        return null;
    }
}
