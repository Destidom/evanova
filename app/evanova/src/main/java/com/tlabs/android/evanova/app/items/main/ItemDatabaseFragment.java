package com.tlabs.android.evanova.app.items.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.views.items.ItemListWidget;
import com.tlabs.android.jeeves.views.items.MarketGroupListWidget;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.eve.api.Item;

import java.util.List;

public class ItemDatabaseFragment extends BaseFragment {

    private MarketGroupListWidget groupsView;
    private ItemListWidget itemsView;

    private ViewFlipper flipper;

    private ItemDatabasePresenter presenter;

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.flipper = new ViewFlipper(getContext());

        this.groupsView = new MarketGroupListWidget(getContext());
        this.groupsView.setListener(new AbstractListRecyclerView.Listener<EveMarketGroup>() {
            @Override
            public void onItemClicked(EveMarketGroup group) {
                presenter.onMarketGroupSelected(group);
            }

            @Override
            public void onItemSelected(EveMarketGroup group, boolean selected) {
                presenter.onMarketGroupSelected(group);
            }

            @Override
            public void onItemMoved(EveMarketGroup group, int from, int to) {

            }
        });

        this.itemsView = new ItemListWidget(getContext());
        this.itemsView.setListener(new AbstractListRecyclerView.Listener<Item>() {
            @Override
            public void onItemClicked(Item item) {
                presenter.onMarketItemSelected(item);
            }

            @Override
            public void onItemSelected(Item item, boolean selected) {
                presenter.onMarketItemSelected(item);
            }

            @Override
            public void onItemMoved(Item item, int from, int to) {

            }
        });
        this.flipper.addView(this.groupsView);
        this.flipper.addView(this.itemsView);
        return this.flipper;
    }

    public void setPresenter(ItemDatabasePresenter presenter) {
        this.presenter = presenter;
    }

    public void setMarketGroups(final List<EveMarketGroup> groups) {
        this.groupsView.setItems(groups);
        this.flipper.setDisplayedChild(0);
    }

    public void setMarketItems(final EveMarketGroup group, final List<Item> items) {
        this.itemsView.setItems(items);
        this.flipper.setDisplayedChild(1);
    }
}
