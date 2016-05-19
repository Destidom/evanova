package com.tlabs.android.jeeves.service.events;

class ContractItemsEvent extends EveApiEvent {

    private final long ownerID;
    private final long contractID;

    protected ContractItemsEvent(final long ownerID, final long contractID) {
        super();
        this.ownerID = ownerID;
        this.contractID = contractID;
    }

    public final long getOwnerID() {
        return ownerID;
    }

    public final long getContractID() {
        return contractID;
    }

}
