package com.tlabs.android.evanova.app.contracts.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.eve.api.Contract;

import java.util.List;

public class ContractListFragment extends BaseFragment {

    private ContractListPager listPager;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        this.listPager = new ContractListPager(getContext());
        return this.listPager;
    }

    public void setContracts(final List<Contract> contracts, long ownerID) {
        this.listPager.setContracts(contracts);
    }
}
