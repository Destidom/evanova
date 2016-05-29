package com.tlabs.android.evanova.app.items.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import java.util.List;

public interface ItemDatabaseView extends ActivityView {

    void showMarketGroups(final List<EveMarketGroup> groups);

    void showMarketItem(Item item);

    void showMarketItems(final EveMarketGroup group, List<Item> items);
}
