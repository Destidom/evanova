package com.tlabs.android.evanova.app.items.impl;

import com.tlabs.android.evanova.app.items.ItemDatabaseUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import java.util.List;

import javax.inject.Inject;

public class ItemDatabaseUseCaseImpl implements ItemDatabaseUseCase {

    private final ContentFacade content;

    @Inject
    public ItemDatabaseUseCaseImpl(final ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<EveMarketGroup> getMarketGroups(final long groupID) {
        return this.content.getMarketGroups(groupID);
    }

    @Override
    public List<Item> getMarketItems(long groupID) {
        return this.content.getMarketItems(groupID);
    }
}
