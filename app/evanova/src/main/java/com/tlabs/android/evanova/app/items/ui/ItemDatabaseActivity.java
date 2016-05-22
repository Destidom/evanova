package com.tlabs.android.evanova.app.items.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.items.DaggerItemDatabaseComponent;
import com.tlabs.android.evanova.app.items.ItemDatabaseModule;
import com.tlabs.android.evanova.app.items.ItemDatabaseView;
import com.tlabs.android.evanova.app.items.presenter.ItemDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveMarketGroup;

import java.util.List;

import javax.inject.Inject;

public class ItemDatabaseActivity extends BaseActivity implements ItemDatabaseView {

    @Inject
    @Presenter
    ItemDatabasePresenter presenter;

    private ItemDatabaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerItemDatabaseComponent.builder()
                .evanovaComponent(Application.getEveComponent())
                .itemDatabaseModule(new ItemDatabaseModule())
                .build()
                .inject(this);

        this.fragment = new ItemDatabaseFragment();
        this.fragment.setPresenter(this.presenter);

        setFragment(this.fragment);
    }

    @Override
    public void onBackPressed() {
        if (!this.presenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void showMarketGroups(List<EveMarketGroup> groups) {
        this.fragment.setMarketGroups(groups);
    }

}
