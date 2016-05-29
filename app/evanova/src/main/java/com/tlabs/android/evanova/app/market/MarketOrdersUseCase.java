package com.tlabs.android.evanova.app.market;

import com.tlabs.eve.api.MarketOrder;

import java.util.List;

public interface MarketOrdersUseCase {
    List<MarketOrder> loadOrders(final long ownerID);
}
