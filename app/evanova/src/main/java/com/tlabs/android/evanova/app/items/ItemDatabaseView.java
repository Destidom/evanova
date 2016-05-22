package com.tlabs.android.evanova.app.items;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveMarketGroup;

import java.util.List;

public interface ItemDatabaseView extends ActivityView {

    void showMarketGroups(final List<EveMarketGroup> groups);

}
