package com.tlabs.android.evanova.app.wallet;

import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletTransaction;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class WalletUseCaseImpl implements WalletUseCase {

    private final ContentFacade content;

    @Inject
    public WalletUseCaseImpl(ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<WalletTransaction> getTransactions(final long ownerID, final int walletID) {
        if (ownerID <= 0) {
            return Collections.emptyList();
        }
        if (null == content.hitCorporation(ownerID)) {
            return content.getWalletTransactions(ownerID);
        }
        return content.getWalletTransactions(ownerID, walletID);
    }

    @Override
    public List<WalletJournalEntry> getJournal(final long ownerID, final int walletID) {
        if (ownerID <= 0) {
            return Collections.emptyList();
        }
        if (null == content.hitCorporation(ownerID)) {
            return content.getWalletJournal(ownerID);
        }
        return content.getWalletJournal(ownerID, walletID);
    }
}
