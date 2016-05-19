package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractBidsResponse;
import com.tlabs.eve.api.ContractListResponse;
import com.tlabs.eve.api.character.CharacterContractBidsRequest;
import com.tlabs.eve.api.character.CharacterContractsRequest;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CharacterContractsAction extends EveAction {
    
    private final long charID;

    public CharacterContractsAction(final Context context, final long charID) {
        super(
            context,
            new CharacterContractsRequest(Long.toString(charID)),
            new CharacterContractBidsRequest(Long.toString(charID)));
        this.charID = charID;}

    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response.getCached()) {
            return null;
        }

        if (request instanceof CharacterContractsRequest) {
            final ContractListResponse r = (ContractListResponse)response;
            evanova.updateContracts(charID, r.getContracts());
            return newNameAction(r.getContracts());
        }

        if (request instanceof CharacterContractBidsRequest) {
            evanova.updateContractBids(charID, ((ContractBidsResponse) response).getBids());
        }
        return null;
    }

    private NamesAction newNameAction(final List<Contract> contracts) {
        if (contracts.isEmpty()) {
            return null;
        }

        final Set<Long> ids = new HashSet<>();
        for (Contract c: contracts) {
            ids.add(c.getAcceptorID());
            ids.add(c.getAssigneeID());
            ids.add(c.getForCorpID());
            ids.add(c.getIssuerCorpID());
            ids.add(c.getIssuerID());
        }
        final List<Long> filtered = this.cache.filterExistingNames(new ArrayList<>(ids));
        return new NamesAction(this, filtered);
    }
}
