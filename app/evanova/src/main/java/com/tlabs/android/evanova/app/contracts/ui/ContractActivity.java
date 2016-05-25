package com.tlabs.android.evanova.app.contracts.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.contracts.ContractView;
import com.tlabs.android.evanova.app.contracts.presenter.ContractPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.eve.api.Contract;

import java.util.List;

import javax.inject.Inject;

public class ContractActivity extends BaseActivity implements ContractView {

    @Inject
    @Presenter
    ContractPresenter presenter;

    private ContractFragment fContract;
    private ContractListFragment fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fContract = new ContractFragment();
        this.fList = new ContractListFragment();
    }

    @Override
    public void showContracts(List<Contract> contracts, long ownerID) {
        stackFragment(this.fList);
        this.fList.setContracts(contracts, ownerID);
    }

    @Override
    public void showContract(Contract contract, long ownerID) {
        stackFragment(this.fContract);
        this.fContract.setContract(contract, ownerID);
    }
}
