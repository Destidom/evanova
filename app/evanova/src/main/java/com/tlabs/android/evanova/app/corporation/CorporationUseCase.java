package com.tlabs.android.evanova.app.corporation;

import com.tlabs.android.jeeves.model.EveCorporation;

import java.util.List;

public interface CorporationUseCase {

    List<EveCorporation> loadCorporations();

    EveCorporation loadCorporation(final long id);
}
