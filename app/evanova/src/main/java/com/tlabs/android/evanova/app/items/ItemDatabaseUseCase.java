package com.tlabs.android.evanova.app.items;

import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import java.util.List;

public interface ItemDatabaseUseCase {

    List<EveMarketGroup> getMarketGroups(final long groupID);

    List<Item> getMarketItems(final long groupID);
}
