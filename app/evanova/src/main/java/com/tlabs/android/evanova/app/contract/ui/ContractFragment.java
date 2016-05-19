package com.tlabs.android.evanova.app.contract.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.eve.api.Contract;

public class ContractFragment extends BaseFragment {

    private ContractPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        this.pager = new ContractPager(getContext());
        return this.pager;
    }

    public void setContract(final Contract contract, final long ownerID) {
        this.pager.setContract(contract, ownerID);
    }
}
