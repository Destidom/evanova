package com.tlabs.android.jeeves.views.contracts;

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
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractBid;

public class ContractBidListWidget extends AbstractListRecyclerView<ContractBid> implements ContractWidget {

    private class ContractBidHolder extends AbstractListRowHolder<ContractBid> {
        private final TextView bidDate;
        private final TextView bidAmount;
        private final TextView bidBidder;

        private ContractBidHolder(final View view) {
            super(view);

            this.bidDate = (TextView)view.findViewById(R.id.j_contract_BidWhen);
            this.bidAmount = (TextView)view.findViewById(R.id.j_contract_BidAmount);
            this.bidBidder = (TextView)view.findViewById(R.id.j_contract_BidBidder);
        }

        @Override
        public void render(ContractBid contractBid) {
            render(contractBid, ownerID, issuerID);
        }

        public final void render(ContractBid bid, final long ownerID, final long issuerID) {
            this.bidDate.setText(EveFormat.DateTime.MEDIUM(bid.getBidDate()));
            this.bidAmount.setText(EveFormat.Currency.MEDIUM.format(bid.getAmount()));
            if(ownerID != issuerID) {
                this.bidAmount.setTextColor(ownerID == bid.getBidderID() ? Color.GREEN : Color.RED);
            }
            else {
                this.bidAmount.setTextColor(Color.WHITE);
            }
            this.bidBidder.setText(bid.getBidderName());
        }
    }

    private long ownerID;
    private long issuerID;

    public ContractBidListWidget(Context context) {
        super(context);
    }

    public ContractBidListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContractBidListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<ContractBid> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContractBidHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_contract_bid, parent, false));
    }

    @Override
    public void setContract(final Contract contract, final long ownerID) {
        this.issuerID = contract.getIssuerID();
        this.ownerID = ownerID;
        setItems(contract.getBids());
    }

    @Override
    protected long getItemId(ContractBid item) {
        return item.getBidID();
    }
}
