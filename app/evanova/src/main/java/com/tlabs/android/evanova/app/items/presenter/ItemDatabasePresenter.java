package com.tlabs.android.evanova.app.items.presenter;

import android.app.Activity;
import android.content.Context;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.items.ItemDatabaseUseCase;
import com.tlabs.android.evanova.app.items.ItemDatabaseView;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ItemDatabasePresenter extends EvanovaActivityPresenter<ItemDatabaseView> {

    private final ItemDatabaseUseCase useCase;
    private final List <EveMarketGroup> groups;

    @Inject
    public ItemDatabasePresenter(
            final Context context,
            final ItemDatabaseUseCase useCase) {
        super(context);
        this.groups = new ArrayList<>();
        this.useCase = useCase;
    }

    @Override
    public void setView(ItemDatabaseView view) {
        super.setView(view);
        setBackgroundDefault();
        this.groups.clear();
   //     this.groups.add(-1l);
        subscribe(() -> this.useCase.getMarketGroups(-1l), groups -> getView().showMarketGroups(groups));
    }

    public boolean onBackPressed() {
        if (this.groups.isEmpty()) {
            return false;
        }

        final EveMarketGroup last = this.groups.remove(this.groups.size() - 1);
        subscribe(() -> this.useCase.getMarketGroups(last.getMarketGroupID()), groups -> getView().showMarketGroups(groups));
        return true;
    }

    public void onMarketGroupSelected(final EveMarketGroup group) {
        if (null == group) {
            this.groups.clear();
            subscribe(() -> this.useCase.getMarketGroups(-1l), groups -> getView().showMarketGroups(groups));
            return;
        }

        this.groups.add(group);
        if (group.getChildCount() == 0) {
            subscribe(() -> {
                final List<Item> items =  this.useCase.getMarketItems(group.getMarketGroupID());
                final List<EveMarketGroup> groups = new ArrayList<>(items.size());
                for (Item item: items) {
                    final EveMarketGroup g = new EveMarketGroup();
                    g.setParentGroupID(group.getMarketGroupID());
                    g.setMarketGroupID(item.getItemID());
                    g.setIconID(item.getItemID());
                    g.setMarketGroupName(item.getName());

                    groups.add(g);
                }
                return groups;
            }, items -> getView().showMarketGroups(items));
        }
        else {
            subscribe(() -> this.useCase.getMarketGroups(group.getMarketGroupID()), groups -> getView().showMarketGroups(groups));

        }
    }

}
