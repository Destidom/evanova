package com.tlabs.android.evanova.app.items.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.items.DaggerItemDatabaseComponent;
import com.tlabs.android.evanova.app.items.ItemDatabaseModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Item;

import java.util.List;

import javax.inject.Inject;

public class ItemDatabaseActivity extends BaseActivity implements ItemDatabaseView {

    @Inject
    @Presenter
    ItemDatabasePresenter presenter;

    private ItemDatabaseFragment databaseFragment;
    private ItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerItemDatabaseComponent.builder()
                .applicationComponent(Application.getAppComponent())
                .itemDatabaseModule(new ItemDatabaseModule())
                .build()
                .inject(this);

        this.databaseFragment = new ItemDatabaseFragment();
        this.databaseFragment.setPresenter(this.presenter);

        this.itemFragment = new ItemFragment();

        setFragment(this.databaseFragment);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        this.presenter.startView(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                this.presenter.onItemSearchSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        this.databaseFragment.setMarketGroups(groups);
    }

    @Override
    public void showMarketItem(Item item) {
        stackFragment(this.itemFragment);
        this.itemFragment.setItem(item);
    }

    @Override
    public void showMarketItems(EveMarketGroup group, List<Item> items) {
        this.databaseFragment.setMarketItems(group, items);
    }
}
