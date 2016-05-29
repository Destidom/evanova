package com.tlabs.android.evanova.app.contracts.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.contracts.ContractUseCase;
import com.tlabs.eve.api.Contract;

import javax.inject.Inject;

public class ContractPresenter extends EvanovaActivityPresenter<ContractView> {

    private final ContractUseCase useCase;
    private long ownerID = -1;

    @Inject
    public ContractPresenter(Context context, ContractUseCase useCase) {
        super(context);
        this.useCase = useCase;

    }

    @Override
    public void setView(ContractView view) {
        super.setView(view);
        setBackgroundDefault();
    }

    @Override
    public void startView(Intent intent) {
        this.ownerID = ownerOf(intent);
        if (this.ownerID == -1l) {
            return;
        }
        showLoading(true);
        subscribe(() -> useCase.loadContracts(this.ownerID), contracts -> {
            getView().setContracts(contracts);
            showLoading(false);
        });
    }

    public void onContractSelected(final Contract contract) {
        showLoading(true);
        subscribe(
            () -> useCase.loadContract(ownerID, contract.getContractID()),
            c -> {
                getView().showContract(c);
                showLoading(false);
            });
    }
}
