package com.tlabs.android.evanova.app.market.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.api.MarketOrder;

import java.util.List;

public interface MarketOrdersView extends ActivityView {

    void setBuyOrders(final List<MarketOrder> orders);

    void setSellOrders(final List<MarketOrder> orders);
}
