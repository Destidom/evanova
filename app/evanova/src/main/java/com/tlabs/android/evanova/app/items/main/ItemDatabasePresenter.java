package com.tlabs.android.evanova.app.items.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.items.ItemDatabaseUseCase;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ItemDatabasePresenter extends EvanovaActivityPresenter<ItemDatabaseView> {
    private static final Logger LOG = LoggerFactory.getLogger(ItemDatabasePresenter.class);
    private final ItemDatabaseUseCase useCase;
    private final List<Long> groups;

    @Inject
    public ItemDatabasePresenter(
            final Context context,
            final ItemDatabaseUseCase useCase) {
        super(context);
        this.useCase = useCase;
        this.groups = new ArrayList<>(15);
    }

    @Override
    public void setView(ItemDatabaseView view) {
        super.setView(view);
        setBackgroundDefault();
        this.groups.clear();
        this.groups.add(-1l);
        subscribe(() -> this.useCase.getMarketGroups(-1l), groups -> getView().showMarketGroups(groups));
    }

    @Override
    public void startView(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            final String query = intent.getDataString();
            if (StringUtils.isBlank(query)) {
                return;
            }
            subscribe(() -> {
                try {
                    return this.useCase.getMarketItem(Long.parseLong(query));
                }
                catch (NumberFormatException e) {
                    return null;
                }
            },
            item -> {
               if (null != item) {
                   onMarketItemSelected(item);
               }
            });
        }
    }

    public boolean onBackPressed() {
        if (this.groups.isEmpty()) {
            return false;
        }

        final long last = this.groups.remove(this.groups.size() - 1);
        subscribe(() -> this.useCase.getMarketGroups(last), groups -> getView().showMarketGroups(groups));
        return true;
    }

    public void onItemSearchSelected() {
        getView().showSearch();
    }

    public void onMarketItemSelected(final Item item) {
        getView().showMarketItem(item);
    }

    public void onMarketGroupSelected(final EveMarketGroup group) {
        if (null == group) {
            this.groups.clear();
            this.groups.add(-1l);
            subscribe(() -> this.useCase.getMarketGroups(-1l), groups -> getView().showMarketGroups(groups));
            return;
        }

        if (group.getChildCount() == 0) {
            subscribe(
                    () -> this.useCase.getMarketItems(group.getMarketGroupID()),
                    items -> getView().showMarketItems(group, items));
        }
        else {
            this.groups.add(group.getMarketGroupID());
            subscribe(
                    () -> this.useCase.getMarketGroups(group.getMarketGroupID()),
                    groups -> getView().showMarketGroups(groups));
        }
    }

}
