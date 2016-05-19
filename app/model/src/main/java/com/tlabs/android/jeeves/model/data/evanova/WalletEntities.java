package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.eve.api.AccountBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WalletEntities {

    private WalletEntities() {
    }

    public static Map<Long, Double> transform(final List<AccountBalance> balance) {
        final Map<Long, Double> returned = new HashMap<>(balance.size());
        for (AccountBalance b: balance) {
            returned.put(b.getAccountKey(), b.getBalance());
        }
        return returned;
    }

}