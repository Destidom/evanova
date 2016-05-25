package com.tlabs.android.evanova.app.corporations;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCorporation;

import java.util.List;

public interface CorporationListView extends ActivityView {

    void showCorporations(final List<EveCorporation> corporations);
}
