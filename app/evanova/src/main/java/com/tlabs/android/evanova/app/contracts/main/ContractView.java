package com.tlabs.android.evanova.app.contracts.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.api.Contract;

import java.util.List;

interface ContractView extends ActivityView {

    void setContracts(final List<Contract> contracts);

    void showContract(final Contract contract);
}
