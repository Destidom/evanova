package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.data.evanova.entities.ContractBidEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractItemEntity;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractBid;
import com.tlabs.eve.api.ContractItem;

public final class ContractEntities {

    private ContractEntities() {
    }


    public static ContractEntity transform(final long ownerID, final Contract contract) {
        final ContractEntity entity = new ContractEntity();
        entity.setOwnerID(ownerID);
        entity.setContractID(contract.getContractID());
        entity.setType(contract.getType().getValue());
        entity.setAcceptorID(contract.getAcceptorID());
        entity.setAssigneeID(contract.getAssigneeID());
        entity.setAvailability(contract.getAvailability());
        entity.setBuyout(contract.getBuyout());
        entity.setCollateral(contract.getCollateral());
        entity.setDateAccepted(contract.getDateAccepted());
        entity.setDateCompleted(contract.getDateCompleted());
        entity.setDateExpired(contract.getDateExpired());
        entity.setDateIssued(contract.getDateIssued());
        entity.setStartStationID(contract.getStartStationID());
        entity.setEndStationID(contract.getEndStationID());
        entity.setForCorpID(contract.getForCorpID());
        entity.setIssuerCorpID(contract.getIssuerCorpID());
        entity.setIssuerID(contract.getIssuerID());
        entity.setNumDays(contract.getNumDays());
        entity.setStatus(contract.getStatus().getValue());
        entity.setPrice(contract.getPrice());
        entity.setReward(contract.getReward());
        entity.setVolume(contract.getVolume());
        entity.setBuyout(contract.getBuyout());

        return entity;
    }

    public static ContractBidEntity transform(final long ownerID, final ContractBid bid) {
        final ContractBidEntity entity = new ContractBidEntity();
        entity.setBidAmount(bid.getAmount());
        entity.setBidDate(bid.getBidDate());
        entity.setBidderID(bid.getBidderID());
        entity.setBidID(bid.getBidID());
        entity.setContractID(bid.getContractID());
        entity.setOwnerID(ownerID);
        return entity;
    }

    public static ContractItemEntity transform(final long ownerID, final long contractID, final ContractItem item) {
        final ContractItemEntity entity = new ContractItemEntity();
        entity.setOwnerID(ownerID);
        entity.setContractID(contractID);
        entity.setRecordID(item.getRecordID());
        entity.setIncluded(item.getIncluded() ? 1 : 0);
        entity.setItemID(item.getTypeID());
        entity.setQuantity(item.getQuantity());
        entity.setRawQuantity(item.getRawQuantity());

        return entity;
    }

    public static Contract transform(ContractEntity entity) {
        if (null == entity) {
            return null;
        }

        final Contract contract = new Contract();
        contract.setContractID(entity.getContractID());
        contract.setType(entity.getType());
        contract.setAcceptorID(entity.getAcceptorID());
        contract.setAssigneeID(entity.getAssigneeID());
        contract.setAvailability(entity.getAvailability());
        contract.setBuyout(entity.getBuyout());
        contract.setCollateral(entity.getCollateral());
        contract.setDateAccepted(entity.getDateAccepted());
        contract.setDateCompleted(entity.getDateCompleted());
        contract.setDateExpired(entity.getDateExpired());
        contract.setDateIssued(entity.getDateIssued());
        contract.setStartStationID(entity.getStartStationID());
        contract.setEndStationID(entity.getEndStationID());
        contract.setForCorpID(entity.getForCorpID());
        contract.setIssuerCorpID(entity.getIssuerCorpID());
        contract.setIssuerID(entity.getIssuerID());
        contract.setNumDays(entity.getNumDays());
        contract.setStatus(entity.getStatus());
        contract.setPrice(entity.getPrice());
        contract.setReward(entity.getReward());
        contract.setVolume(entity.getVolume());
        contract.setBuyout(entity.getBuyout());

        //contract.setItems();
        //contract.setBids();

        return contract;
    }

}