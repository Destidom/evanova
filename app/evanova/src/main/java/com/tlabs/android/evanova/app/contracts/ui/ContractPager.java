package com.tlabs.android.evanova.app.contracts.ui;


import android.content.Context;
import android.util.AttributeSet;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.contracts.ContractBidListWidget;
import com.tlabs.android.jeeves.views.contracts.ContractDetailsWidget;
import com.tlabs.android.jeeves.views.contracts.ContractItemListWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Contract;

public class ContractPager extends ViewPager {

    private ViewPagerAdapter adapter;

    private ContractDetailsWidget detailsWidget;
    private ContractItemListWidget itemsWidget;
    private ContractBidListWidget bidsWidget;

    public ContractPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContractPager(Context context) {
        super(context);
        init();
    }

    public void setContract(final Contract contract, final long ownerID) {
        this.detailsWidget.setContract(contract, ownerID);
        this.itemsWidget.setContract(contract, ownerID);
        this.bidsWidget.setContract(contract, ownerID);
    }

    private void init() {
        setId(R.id.pagerContract);

        this.adapter = new ViewPagerAdapter(getContext());

        this.detailsWidget = new ContractDetailsWidget(getContext());
        this.adapter.addView(this.detailsWidget, R.string.jeeves_contract_page_details);

        this.itemsWidget = new ContractItemListWidget(getContext());
        this.adapter.addView(this.itemsWidget, R.string.jeeves_contract_page_items);

        this.bidsWidget = new ContractBidListWidget(getContext());
        this.adapter.addView(this.bidsWidget, R.string.jeeves_contract_page_bids);

        setAdapter(this.adapter);
    }
}
