package com.tlabs.android.jeeves.views.wallet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.WalletTransaction;

public class WalletTransactionsWidget extends AbstractListRecyclerView<WalletTransaction> {

    private static final class TransactionHolder extends ListRecyclerViewAdapter.ViewHolder {
        private static final String TRANSACTION_TYPE_BUY = "buy";

       /* @BindView(id.walletTransactionClient)
        TextView clientView;
        @BindView(id.walletTransactionWhen) TextView whenView;
        @BindView(id.walletTransactionItem) TextView itemView;
        @BindView(id.walletTransactionQuantity) TextView quantityView;
        @BindView(id.walletTransactionPrice) TextView priceView;
        @BindView(id.walletTransactionCredit) TextView creditView;
        @BindView(id.walletTransactionType) TextView typeView;*/

        public TransactionHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);
        }

        public void bind(final WalletTransaction t) {

            final boolean buy = TRANSACTION_TYPE_BUY.equalsIgnoreCase(t.getType());

           /* whenView.setText(FormatHelper.DateTime.LONG(t.getWhen(), false));
            clientView.setText(t.getClientName());
            itemView.setText(t.getTypeName());
            quantityView.setText(quantityView.getResources().getString(string.wallet_transaction_qty, t.getQuantity()));
            priceView.setText(FormatHelper.Currency.LONG(t.getPrice()));

            if (buy) {
                creditView.setText(FormatHelper.Currency.LONG(-1d * t.getPrice() * t.getQuantity()));
                creditView.setTextColor(Color.RED);
            }
            else {
                creditView.setText(FormatHelper.Currency.LONG(t.getPrice() * t.getQuantity()));
                creditView.setTextColor(Color.GREEN);
            }
            typeView.setText(typeView.getResources().getString(buy ? R.string.wallet_transaction_seller : R.string.wallet_transaction_buyer));
            */
        }
    }
    public WalletTransactionsWidget(Context context) {
        super(context);
    }

    public WalletTransactionsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalletTransactionsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<WalletTransaction> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_wallet_transaction, parent, false));
    }

    @Override
    protected long getItemId(WalletTransaction item) {
        return item.getID();
    }
}
