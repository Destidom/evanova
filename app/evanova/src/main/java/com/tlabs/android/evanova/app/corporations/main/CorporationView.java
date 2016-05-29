package com.tlabs.android.evanova.app.corporations.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCorporation;

public interface CorporationView extends ActivityView {

    void setCorporation(final EveCorporation corporation);

    void showCorporationInformation();

}
