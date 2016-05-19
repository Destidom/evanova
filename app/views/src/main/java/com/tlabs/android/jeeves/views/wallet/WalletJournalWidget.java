package com.tlabs.android.jeeves.views.wallet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.WalletJournalEntry;

public class WalletJournalWidget extends AbstractListRecyclerView<WalletJournalEntry> {

    private static final class JournalEntryHolder extends ListRecyclerViewAdapter.ViewHolder {

       // @BindView(id.walletJournalAmount)
        TextView amountView;
     //   @BindView(id.walletJournalBalance) TextView balanceView;
     //   @BindView(id.walletJournalOwner1) TextView owner1View;
     //   @BindView(id.walletJournalOwner2) TextView owner2View;
    //    @BindView(id.walletJournalReason) TextView reasonView;
    //    @BindView(id.walletJournalType) TextView typeView;
    //    @BindView(id.walletJournalWhen) TextView whenView;

        public JournalEntryHolder(View view) {
            super(view);
    //        ButterKnife.bind(this, view);
        }

        public void bind(WalletJournalEntry e) {
          /*  whenView.setText(FormatHelper.DateTime.LONG(e.getWhen(), false));
            amountView.setText(FormatHelper.Currency.LONG(e.getAmount()));
            balanceView.setText(FormatHelper.Currency.LONG(e.getBalance()));
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
            }*/
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
