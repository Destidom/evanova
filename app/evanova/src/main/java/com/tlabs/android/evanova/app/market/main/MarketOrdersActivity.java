package com.tlabs.android.evanova.app.market.main;

import android.content.Context;
import android.os.Bundle;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.market.DaggerMarketOrdersComponent;
import com.tlabs.android.evanova.app.market.MarketOrdersModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.views.market.MarketOrderListWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.MarketOrder;

import java.util.List;

import javax.inject.Inject;

public class MarketOrdersActivity extends BaseActivity implements MarketOrdersView {

    private static class MarketOrdersAdapter extends ViewPagerAdapter {
        public MarketOrdersAdapter(Context context) {
            super(context);
            addView(new MarketOrderListWidget(context), R.string.jeeves_contract_page_current);
            addView(new MarketOrderListWidget(context), R.string.jeeves_contract_page_current);
        }

        public void setBuyOrders(List<MarketOrder> orders) {
            MarketOrderListWidget w = getView(0);
            w.setItems(orders);
        }

        public void setSellOrders(List<MarketOrder> orders) {
            MarketOrderListWidget w = getView(1);
            w.setItems(orders);
        }
    }

    @Inject
    @Presenter
    MarketOrdersPresenter presenter;

    private TabPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMarketOrdersComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .marketOrdersModule(new MarketOrdersModule())
                .build()
                .inject(this);

        this.pager = new TabPager(this);
        this.pager.setAdapter(new MarketOrdersAdapter(this));

        setView(this.pager);
    }

    @Override
    public void setBuyOrders(List<MarketOrder> orders) {
        MarketOrdersAdapter adapter = pager.getAdapter();
        adapter.setBuyOrders(orders);
    }

    @Override
    public void setSellOrders(List<MarketOrder> orders) {
        MarketOrdersAdapter adapter = pager.getAdapter();
        adapter.setSellOrders(orders);
    }
}
