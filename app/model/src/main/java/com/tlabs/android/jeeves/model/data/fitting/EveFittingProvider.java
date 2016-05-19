package com.tlabs.android.jeeves.model.data.fitting;

import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.eve.dogma.FittingProvider;
import com.tlabs.eve.dogma.model.Item;
import com.tlabs.eve.dogma.ormlite.OrmLiteFittingProvider;

import java.sql.SQLException;

final class EveFittingProvider implements FittingProvider {

    private FittingProvider fitting;

    public EveFittingProvider(final ConnectionSource source) throws SQLException {
        this.fitting = new OrmLiteFittingProvider(source);
    }

    @Override
    public Item findItem(long l) {
        return fitting.findItem(l);
    }

    @Override
    public Item findItem(String s) {
        return fitting.findItem(s);
    }
}
