package com.tlabs.android.evanova.app.contracts;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.api.Contract;

import java.util.List;

public interface ContractView extends ActivityView {

    void showContracts(final List<Contract> contracts, final long ownerID);

    void showContract(final Contract contract, final long ownerID);
}
