package com.tlabs.android.jeeves.views.contracts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tlabs.eve.api.Contract;

public class ContractDetailsWidget extends FrameLayout implements ContractWidget {

    private ContractDetailsHolder holder;

    public ContractDetailsWidget(Context context) {
        super(context);
        init();
    }

    public ContractDetailsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContractDetailsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setContract(final Contract contract, final long ownerID) {
        this.holder.render(contract, ownerID);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_contract_details, this);
        this.holder = new ContractDetailsHolder(this);
    }

}
