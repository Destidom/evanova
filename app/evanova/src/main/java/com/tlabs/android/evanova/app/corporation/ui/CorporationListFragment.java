package com.tlabs.android.evanova.app.corporation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.corporation.CorporationListWidget;

import java.util.List;

public class CorporationListFragment extends BaseFragment {

    private CorporationListWidget listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        this.listView = new CorporationListWidget(getContext());
        return this.listView;
    }

    public void setCorporations(final List<EveCorporation> corporations) {
        this.listView.setItems(corporations);
    }
}
