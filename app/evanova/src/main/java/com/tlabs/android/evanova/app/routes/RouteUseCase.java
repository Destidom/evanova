package com.tlabs.android.evanova.app.routes;

import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;

import java.util.List;

public interface RouteUseCase {

    DotlanRoute loadRoute(final DotlanOptions options, final int type);

    DotlanRoute loadJumpRoute(final DotlanJumpOptions options);

    void saveOptions(final DotlanOptions options);

    void saveOptions(final List<DotlanOptions> options);

    List<DotlanOptions> loadOptions();

    boolean updateLiveRoute(final DotlanRoute route);

    DotlanRoute loadLiveRoute(final DotlanRoute route);
}
