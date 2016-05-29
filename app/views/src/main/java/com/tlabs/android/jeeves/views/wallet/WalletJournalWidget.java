package com.tlabs.android.jeeves.views.wallet;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.WalletJournalEntry;

import org.apache.commons.lang3.StringUtils;

public class WalletJournalWidget extends AbstractListRecyclerView<WalletJournalEntry> {

    private static final class JournalEntryHolder extends AbstractListRowHolder<WalletJournalEntry> {

        private final TextView amountView;
        private final TextView balanceView;
        private final TextView owner1View;
        private final TextView owner2View;
        private final TextView reasonView;
        private final TextView typeView;
        private final TextView whenView;

        public JournalEntryHolder(View view) {
            super(view);
            this.amountView = findView(R.id.j_walletJournalAmount);
            this.balanceView = findView(R.id.j_walletJournalBalance);
            this.owner1View = findView(R.id.j_walletJournalOwner1);
            this.owner2View = findView(R.id.j_walletJournalOwner2);
            this.reasonView = findView(R.id.j_walletJournalReason);
            this.typeView = findView(R.id.j_walletJournalType);
            this.whenView = findView(R.id.j_walletJournalWhen);
        }

        @Override
        public void render(WalletJournalEntry e) {
            whenView.setText(EveFormat.DateTime.LONG(e.getWhen(), false));
            amountView.setText(EveFormat.Currency.LONG(e.getAmount()));
            balanceView.setText(EveFormat.Currency.LONG(e.getBalance()));
            owner1View.setText(e.getOwnerName1());
            owner2View.setText(e.getOwnerName2());
            typeView.setText(e.getRefTypeName());

            if((e.getRefTypeID() == 10 || e.getRefTypeID() == 37) && StringUtils.isNotBlank(e.getReason())) {
                reasonView.setVisibility(View.VISIBLE);
                reasonView.setText(e.getReason());
            } else {
                reasonView.setVisibility(View.GONE);
            }

            if (e.getAmount() < 0) {
                amountView.setTextColor(Color.RED);
            }
            else {
                amountView.setTextColor(Color.GREEN);
            }
        }
    }

    public WalletJournalWidget(Context context) {
        super(context);
    }

    public WalletJournalWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalletJournalWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<WalletJournalEntry> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JournalEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_wallet_journal, parent, false));
    }

    @Override
    protected long getItemId(WalletJournalEntry item) {
        return item.getId();
    }
}
