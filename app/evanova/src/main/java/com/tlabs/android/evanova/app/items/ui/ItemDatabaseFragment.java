package com.tlabs.android.evanova.app.items.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.app.items.presenter.ItemDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.views.items.MarketGroupListView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;

import java.util.List;

public class ItemDatabaseFragment extends BaseFragment {

    private MarketGroupListView listView;
    private ItemDatabasePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.listView = new MarketGroupListView(getContext());
        this.listView.setListener(new AbstractListRecyclerView.Listener<EveMarketGroup>() {
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
        return listView;
    }

    public void setPresenter(ItemDatabasePresenter presenter) {
        this.presenter = presenter;
    }

    public void setMarketGroups(final List<EveMarketGroup> groups) {
        this.listView.setItems(groups);
    }
}
