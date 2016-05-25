package com.tlabs.android.evanova.app.contracts.presenter;

import com.tlabs.android.evanova.app.contracts.ContractUseCase;
import com.tlabs.android.evanova.app.contracts.ContractView;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.jeeves.model.EveAccount;


import javax.inject.Inject;

public class ContractPresenter extends ViewPresenter<ContractView> {

    private final ContractUseCase useCase;
    private final EveAccount owner;

    @Inject
    public ContractPresenter(ContractUseCase useCase, EveAccount account) {
        this.useCase = useCase;
        this.owner = account;
    }

    @Override
    public void setView(ContractView view) {
        super.setView(view);
        if (null != this.owner) {
            view.showLoading(true);
            subscribe(() -> useCase.loadContracts(this.owner.getOwnerId()), contracts -> {
                view.showLoading(false);
                view.showContracts(contracts, this.owner.getOwnerId());
            });
        }
    }

    public void setContract(final long contractID) {
        if (null == this.owner) {
            return;
        }
        getView().showLoading(true);
        subscribe(() -> useCase.loadContract(owner.getOwnerId(), contractID), c -> {
            getView().showLoading(false);
            getView().showContract(c, this.owner.getOwnerId());
        });
    }
}
