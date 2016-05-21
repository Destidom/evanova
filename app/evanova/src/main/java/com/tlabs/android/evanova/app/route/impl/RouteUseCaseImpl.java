package com.tlabs.android.evanova.app.route.impl;

import com.tlabs.android.evanova.app.route.RouteUseCase;
import com.tlabs.android.evanova.preferences.SavedPreferences;

import org.devfleet.crest.CrestService;
import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;
import org.devfleet.dotlan.DotlanService;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class RouteUseCaseImpl implements RouteUseCase {

    private final Lazy<DotlanService> dotlan;
    private final Lazy<CrestService> crest;

    private final SavedPreferences preferences;

    @Inject
    public RouteUseCaseImpl(Lazy<DotlanService> dotlan, Lazy<CrestService> crest, SavedPreferences preferences) {
        this.dotlan = dotlan;
        this.crest = crest;
        this.preferences = preferences;
    }

    @Override
    public DotlanRoute loadRoute(DotlanOptions options, int type) {
        switch (type) {
            case 1:
                return this.dotlan.get().getHighSecRoute(options);
            case 2:
                return this.dotlan.get().getLowSecRoute(options);
            default:
                return this.dotlan.get().getFastestRoute(options);
        }
    }

    @Override
    public DotlanRoute loadJumpRoute(DotlanJumpOptions options) {
        return this.dotlan.get().getJumpRoute(options);
    }

    @Override
    public void saveOptions(DotlanOptions options) {
        final List<DotlanOptions> saved = loadOptions();
        DotlanOptions existing = null;
        for (DotlanOptions o: saved) {
            if (o.getTo().equals(options.getTo()) && o.getFrom().equals(options.getFrom())) {
                existing = o;
                break;
            }
        }
        if (null != existing) {
            saved.remove(existing);
        }
        saved.add(0, options);
        this.preferences.setMostRecentRoutes(saved);
    }

    @Override
    public void saveOptions(List<DotlanOptions> options) {
        this.preferences.setMostRecentRoutes(options);
    }

    @Override
    public List<DotlanOptions> loadOptions() {
        return this.preferences.getMostRecentRoutes();
    }

    @Override
    public boolean updateLiveRoute(DotlanRoute route) {
        return false;
    }

    @Override
    public DotlanRoute loadLiveRoute(DotlanRoute route) {
        return null;
    }
}
