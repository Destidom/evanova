package com.tlabs.android.evanova.app.corporation.impl;

import com.tlabs.android.evanova.app.corporation.CorporationUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveCorporation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CorporationUseCaseImpl implements CorporationUseCase {

    private final ContentFacade content;

    @Inject
    public CorporationUseCaseImpl(ContentFacade content) {
        this.content = content;
    }

    @Override
    public List<EveCorporation> loadCorporations() {
        final List<EveCorporation> corporations = new ArrayList<>();
        for (Long id: content.listCorporations()) {
            corporations.add(content.getCorporation(id));
        }
        return corporations;
    }

    @Override
    public EveCorporation loadCorporation(long id) {
        return content.getCorporation(id);
    }
}
