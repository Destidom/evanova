package com.tlabs.android.evanova.app.contract;

import com.tlabs.eve.api.Contract;

import java.util.List;

public interface ContractUseCase {

    List<Contract> loadContracts(final long ownerID);

    Contract loadContract(final long ownerID, final long contractID);
}
