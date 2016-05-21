package com.tlabs.android.evanova.app.items.presenter;

import android.content.Context;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.items.ItemDatabaseView;

import javax.inject.Inject;

public class ItemDatabasePresenter extends EvanovaActivityPresenter<ItemDatabaseView> {

    @Inject
    public ItemDatabasePresenter(Context context) {
        super(context);
    }

    @Override
    public void setView(ItemDatabaseView view) {
        super.setView(view);
        setBackgroundDefault();
    }
}
