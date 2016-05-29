package com.tlabs.android.evanova.app.corporations.list;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporations.CorporationModule;
import com.tlabs.android.evanova.app.corporations.DaggerCorporationComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.corporation.CorporationListWidget;

import java.util.List;

import javax.inject.Inject;

public class CorporationListActivity extends BaseActivity implements CorporationListView {

    @Inject
    @Presenter
    CorporationListPresenter presenter;

    private CorporationListWidget listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCorporationComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .corporationModule(new CorporationModule())
                .build()
                .inject(this);

        this.listView = new CorporationListWidget(this);
        this.listView.setListener(new CorporationListWidget.Listener() {
            @Override
            public void onItemClicked(EveCorporation corporation) {
                presenter.onCorporationSelected(corporation.getID());
            }
        });
        setView(this.listView);
    }

    @Override
    public void showCorporations(List<EveCorporation> corporations) {
        this.listView.setItems(corporations);
    }
}
