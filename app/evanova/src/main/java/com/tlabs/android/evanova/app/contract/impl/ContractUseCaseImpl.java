package com.tlabs.android.evanova.app.contract.impl;

import com.tlabs.android.evanova.app.contract.ContractUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.eve.api.Contract;

import java.util.List;

import javax.inject.Inject;

public class ContractUseCaseImpl implements ContractUseCase {

    private ContentFacade content;

    @Inject
    public ContractUseCaseImpl(ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<Contract> loadContracts(long ownerID) {
        return content.getContracts(ownerID);
    }

    @Override
    public Contract loadContract(long ownerID, long contractID) {
        return content.getContract(ownerID, contractID);
    }
}
