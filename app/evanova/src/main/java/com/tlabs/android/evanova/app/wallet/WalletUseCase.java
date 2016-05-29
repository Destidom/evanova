package com.tlabs.android.evanova.app.wallet;

import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletTransaction;

import java.util.List;

public interface WalletUseCase {

    List<WalletTransaction> getTransactions(final long ownerID, final int walletID);

    List<WalletJournalEntry> getJournal(final long ownerID, final int walletID);
}
