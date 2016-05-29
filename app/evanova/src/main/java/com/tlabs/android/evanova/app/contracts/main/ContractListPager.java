package com.tlabs.android.evanova.app.contracts.main;


import android.content.Context;
import android.util.AttributeSet;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.contracts.ContractListWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Contract;

import java.util.List;

public class ContractListPager extends ViewPager {

    private ViewPagerAdapter adapter;

    public ContractListPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContractListPager(Context context) {
        super(context);
        init();
    }

    public void setContracts(final List<Contract> contracts) {
        final int viewCount = this.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            final ContractListWidget w = (ContractListWidget)getChildAt(viewCount);
            w.setItems(contracts);
        }
    }

    private void init() {
        setId(R.id.pagerContractList);

        this.adapter = new ViewPagerAdapter(getContext());

        final ContractListWidget w1 = new ContractListWidget(getContext());
        w1.setViewType(ContractListWidget.VIEW_ALL);
        this.adapter.addView(w1, R.string.jeeves_contract_page_all);

        final ContractListWidget w2 = new ContractListWidget(getContext());
        w2.setViewType(ContractListWidget.VIEW_CURRENT);
        this.adapter.addView(w2, R.string.jeeves_contract_page_current);

        final ContractListWidget w3 = new ContractListWidget(getContext());
        w3.setViewType(ContractListWidget.VIEW_OUTSTANDING);
        this.adapter.addView(w3, R.string.jeeves_contract_page_outstanding);

        setAdapter(this.adapter);
    }
}
