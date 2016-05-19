package com.tlabs.android.evanova.app.fitting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.app.fitting.presenter.FittingListPresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.jeeves.views.fittings.FittingListWidget;

import javax.inject.Inject;

public class FittingListFragment extends BaseFragment {

    @Inject
    @UserScope
    FittingListPresenter presenter;

    private FittingListWidget listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.listView = new FittingListWidget(getContext());
   //     this.presenter.setView(this.listView);
    //    this.presenter.loadFittings();
        return this.listView;
    }



}
