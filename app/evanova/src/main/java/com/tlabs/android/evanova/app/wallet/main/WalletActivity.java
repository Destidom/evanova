package com.tlabs.android.evanova.app.wallet.main;

import android.content.Context;
import android.os.Bundle;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.wallet.DaggerWalletComponent;
import com.tlabs.android.evanova.app.wallet.WalletModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.android.jeeves.views.wallet.WalletJournalWidget;
import com.tlabs.android.jeeves.views.wallet.WalletTransactionsWidget;
import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletTransaction;

import java.util.List;

import javax.inject.Inject;

public class WalletActivity extends BaseActivity implements WalletView {

    private static class WalletPagerAdapter extends ViewPagerAdapter {
        public WalletPagerAdapter(Context context) {
            super(context);

            addView(new WalletTransactionsWidget(context), R.string.jeeves_contract_page_current);
            addView(new WalletJournalWidget(context), R.string.jeeves_contract_page_current);
        }

        public void setWalletTransactions(List<WalletTransaction> transactions) {
            final WalletTransactionsWidget w = getView(0);
            w.setItems(transactions);
        }

        public void setWalletJournal(List<WalletJournalEntry> journal) {
            final WalletJournalWidget w = getView(1);
            w.setItems(journal);
        }
    }

    @Inject
    @Presenter
    WalletPresenter presenter;

    private TabPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerWalletComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .walletModule(new WalletModule())
                .build()
                .inject(this);

        this.pager = new TabPager(this);
        this.pager.setAdapter(new WalletPagerAdapter(this));
        this.setView(this.pager);
    }

    @Override
    public void setWalletTransactions(List<WalletTransaction> transactions) {
        final WalletPagerAdapter adapter = this.pager.getAdapter();
        adapter.setWalletTransactions(transactions);
    }

    @Override
    public void setWalletJournal(List<WalletJournalEntry> journal) {
        final WalletPagerAdapter adapter = this.pager.getAdapter();
        adapter.setWalletJournal(journal);
    }
}
