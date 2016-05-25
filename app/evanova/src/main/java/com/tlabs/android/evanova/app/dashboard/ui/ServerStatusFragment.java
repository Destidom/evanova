package com.tlabs.android.evanova.app.dashboard.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.views.server.ServerStatusWidget;
import com.tlabs.eve.ccp.EveRSSEntry;

import org.devfleet.crest.model.CrestServerStatus;

import java.util.List;

public class ServerStatusFragment extends BaseFragment {

    private ServerStatusWidget statusView;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
    	this.statusView = new ServerStatusWidget(getContext());

        return this.statusView;
    }

    public void setServerStatus(final CrestServerStatus status) {
        this.statusView.setServerStatus(status);
    }

    public void setRSS(final List<EveRSSEntry> rss) {
        this.statusView.setServerNews(rss);
    }
}
