package com.tlabs.android.evanova.app.contract.presenter;

import com.tlabs.android.evanova.app.contract.ContractUseCase;
import com.tlabs.android.evanova.app.contract.ContractView;
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
            view.setLoading(true);
            subscribe(() -> useCase.loadContracts(this.owner.getOwnerId()), contracts -> {
                view.setLoading(false);
                view.showContracts(contracts, this.owner.getOwnerId());
            });
        }
    }

    public void setContract(final long contractID) {
        if (null == this.owner) {
            return;
        }
        getView().setLoading(true);
        subscribe(() -> useCase.loadContract(owner.getOwnerId(), contractID), c -> {
            getView().setLoading(false);
            getView().showContract(c, this.owner.getOwnerId());
        });
    }
}
