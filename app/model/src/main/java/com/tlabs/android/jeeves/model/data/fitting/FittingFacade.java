package com.tlabs.android.jeeves.model.data.fitting;

import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.Fitting;

import java.io.InputStream;
import java.util.List;

public interface FittingFacade {

    List<Fitting> list();

    List<Fitting> load(final InputStream in);

    Fitting load(final long fittingID);

    Fitting build(final long itemID);

    Fitter fit(final Fitting fitting);

    boolean save(final Fitting fitting);

    boolean saveFittingOrder(final List<Long> fittings);

    boolean delete(final long fittingID);

}
