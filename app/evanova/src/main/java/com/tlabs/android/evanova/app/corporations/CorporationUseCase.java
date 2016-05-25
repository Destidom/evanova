package com.tlabs.android.evanova.app.corporations;

import com.tlabs.android.jeeves.model.EveCorporation;

import java.util.List;

public interface CorporationUseCase {

    List<EveCorporation> loadCorporations();

    EveCorporation loadCorporation(final long id);
}
