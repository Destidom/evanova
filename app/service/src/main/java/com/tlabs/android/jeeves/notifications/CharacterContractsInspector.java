package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.character.CharacterSheet;

import java.util.List;

final class CharacterContractsInspector extends CharacterInspector {

    private final EvanovaFacade facade;

    public CharacterContractsInspector(Context base, final EvanovaFacade facade, final CharacterSheet sheet) {
        super(base, sheet);
        this.facade = facade;
    }

    @Override
    Notification inspect(CharacterSheet sheet) {
        if (!getPreferences().getContractNotificationOption(sheet.getCharacterID())) {
            return null;
        }
        List<Long> contracts = facade.getContracts(sheet.getCharacterID(), System.currentTimeMillis() + 24L * 3600L * 1000L);
        if (contracts.isEmpty()) {
            return null;
        }

        if (contracts.size() > 0) {
            return Notification.from(this, sheet).
                    setTitle(sheet.getCharacterName() + " Contracts").
                    setText(contracts.size() + " contracts expiring within 24h");
        }

        final Contract contract = facade.getContract(sheet.getCharacterID(), contracts.get(0));
        if (null == contract) {
            return null;
        }

        final long remaining  = contract.getDateExpired() - System.currentTimeMillis();
        return Notification.from(this, sheet).
                setTitle(sheet.getCharacterName() + " Contracts").
                setWhen(contract.getDateExpired()).
                setCount(contracts.size()).
                setText("One contract expiring in " + FormatHelper.Duration.MEDIUM(remaining));
    }
}
