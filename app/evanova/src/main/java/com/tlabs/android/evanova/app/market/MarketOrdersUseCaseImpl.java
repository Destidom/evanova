package com.tlabs.android.evanova.app.market;

import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.eve.api.MarketOrder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MarketOrdersUseCaseImpl implements MarketOrdersUseCase {

    private final ContentFacade content;

    @Inject
    public MarketOrdersUseCaseImpl(final ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<MarketOrder> loadOrders(final long ownerID) {
        return (ownerID > 0) ? content.getMarketOrders(ownerID) : Collections.emptyList();
    }
}
