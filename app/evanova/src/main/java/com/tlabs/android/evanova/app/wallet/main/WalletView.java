package com.tlabs.android.evanova.app.wallet.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletTransaction;

import java.util.List;

public interface WalletView extends ActivityView {

    void setWalletTransactions(final List<WalletTransaction> transactions);

    void setWalletJournal(final List<WalletJournalEntry> journal);
}
