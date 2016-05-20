package com.tlabs.android.jeeves.views.contracts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.eve.api.Contract;

public class ContractItemListWidget extends ExpandableListView implements ContractWidget {

    private ContractItemAdapter adapter;

    public ContractItemListWidget(Context context) {
        super(context);
        init();
    }

    public ContractItemListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContractItemListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setContract(final Contract contract, final long ownerID) {
        this.adapter.setItems(contract.getItems(), ownerID == contract.getIssuerID());
    }

    private void init() {
        this.adapter = new ContractItemAdapter();
        setAdapter(this.adapter);
    }

}
