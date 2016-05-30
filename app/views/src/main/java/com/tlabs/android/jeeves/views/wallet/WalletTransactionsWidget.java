package com.tlabs.android.jeeves.views.wallet;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.WalletTransaction;

public class WalletTransactionsWidget extends AbstractListRecyclerView<WalletTransaction> {

    private static final class TransactionHolder extends AbstractListRowHolder<WalletTransaction> {

        private final TextView clientView;
        private final TextView whenView;
        private final TextView itemView;
        private final TextView quantityView;
        private final TextView priceView;
        private final TextView creditView;
        private final TextView typeView;

        public TransactionHolder(View view) {
            super(view);

            this.clientView = findView(R.id.j_walletTransactionClient);
            this.whenView = findView(R.id.j_walletTransactionWhen);
            this.itemView = findView(R.id.j_walletTransactionItem);
            this.quantityView = findView(R.id.j_walletTransactionQuantity);
            this.priceView = findView(R.id.j_walletTransactionPrice);
            this.creditView = findView(R.id.j_walletTransactionCredit);
            this.typeView = findView(R.id.j_walletTransactionType);
        }

        @Override
        public void render(final WalletTransaction t) {

            whenView.setText(EveFormat.DateTime.LONG(t.getWhen(), false));
            clientView.setText(t.getClientName());
            itemView.setText(t.getTypeName());
            quantityView.setText(quantityView.getResources().getString(
                    R.string.jeeves_wallet_transaction_qty, t.getQuantity()));
            priceView.setText(EveFormat.Currency.LONG(t.getPrice()));

            if ("buy".equalsIgnoreCase(t.getType())) {
                creditView.setText(EveFormat.Currency.LONG(-1d * t.getPrice() * t.getQuantity()));
                creditView.setTextColor(Color.RED);
                typeView.setText(R.string.jeeves_wallet_transaction_seller);
            }
            else {
                creditView.setText(EveFormat.Currency.LONG(t.getPrice() * t.getQuantity()));
                creditView.setTextColor(Color.GREEN);
                typeView.setText(R.string.jeeves_wallet_transaction_buyer);
            }
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
        return new TransactionHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_wallet_transaction, parent, false));
    }

    @Override
    protected long getItemId(WalletTransaction item) {
        return item.getID();
    }
}
