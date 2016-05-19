package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.List;

final class CorporationContractsInspector extends CorporationInspector {

    private EvanovaFacade facade;

    public CorporationContractsInspector(Context base, final EvanovaFacade facade, final CorporationSheet sheet) {
        super(base, sheet);
        this.facade = facade;
    }

    @Override
    Notification inspect(CorporationSheet sheet) {
        if (!getPreferences().getContractNotificationOption(sheet.getCorporationID())) {
            return null;
        }
        List<Long> contracts = facade.getContracts(sheet.getCorporationID(), System.currentTimeMillis() + 24L * 3600L * 1000L);
        if (contracts.isEmpty()) {
            return null;
        }

        if (contracts.size() > 0) {
            return Notification.from(this, sheet).
                    setSubtitle(sheet.getCorporationName() + " Contracts").
                    setCount(contracts.size()).
                    setText(contracts.size() + " contracts expiring within 24h");
        }

        final Contract contract = facade.getContract(sheet.getCorporationID(), contracts.get(0));
        if (null == contract) {
            return null;
        }

        final long remaining  = contract.getDateExpired() - System.currentTimeMillis();
        return Notification.from(this, sheet).
                setSubtitle(sheet.getCorporationName() + " Contracts").
                setWhen(contract.getDateExpired()).
                setText("One contract expiring in " + FormatHelper.Duration.MEDIUM(remaining));
    }

}
