package com.tlabs.android.evanova.app.route;

import com.tlabs.android.evanova.mvp.ActivityView;

import org.devfleet.dotlan.DotlanOptions;

import java.util.List;

public interface RouteInputView extends ActivityView {

    void displayOptions(final List<DotlanOptions> options);
}
