package com.tlabs.android.evanova.app.market.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.market.MarketOrdersUseCase;
import com.tlabs.eve.api.MarketOrder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MarketOrdersPresenter extends EvanovaActivityPresenter<MarketOrdersView> {

    private final MarketOrdersUseCase useCase;

    @Inject
    public MarketOrdersPresenter(Context context, MarketOrdersUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void startView(Intent intent) {
        showLoading(true);
        subscribe(() -> {
            final List<MarketOrder> orders = useCase.loadOrders(ownerOf(intent));
            final List<MarketOrder> buyOrders = new ArrayList<>(orders.size() / 2);
            final List<MarketOrder> sellOrders = new ArrayList<>(orders.size() / 2);
            for (MarketOrder order: orders) {
                if (order.getIsBuyOrder()) {
                    buyOrders.add(order);
                }
                else {
                    sellOrders.add(order);
                }
            }
            return new Object[]{buyOrders, sellOrders};
         },
        orders -> {
            showLoading(false);
            getView().setBuyOrders((List<MarketOrder>)orders[0]);
            getView().setSellOrders((List<MarketOrder>)orders[1]);
        });
    }
}
