package com.tlabs.android.evanova.app.corporations;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCorporation;

public interface CorporationView extends ActivityView {

    void showCorporation(final EveCorporation corporation);
}
