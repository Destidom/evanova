package com.tlabs.android.evanova.app.items.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.items.DaggerItemDatabaseComponent;
import com.tlabs.android.evanova.app.items.ItemDatabaseModule;
import com.tlabs.android.evanova.app.items.ItemDatabaseView;
import com.tlabs.android.evanova.app.items.presenter.ItemDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import javax.inject.Inject;

public class ItemDatabaseActivity extends BaseActivity implements ItemDatabaseView {

    @Inject
    ItemDatabasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerItemDatabaseComponent.builder()
                .evanovaComponent(Application.getEveComponent())
                .itemDatabaseModule(new ItemDatabaseModule())
                .build()
                .inject(this);

        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

}
