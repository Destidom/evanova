package com.tlabs.android.evanova.app.fitting.impl;


import com.tlabs.android.evanova.app.fitting.FittingUseCase;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.eve.dogma.Fitting;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestFitting;
import org.devfleet.crest.model.CrestInventory;
import org.devfleet.crest.model.CrestItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class FittingUseCaseImpl implements FittingUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(FittingUseCaseImpl.class);

    private final FittingFacade facade;
    private final Lazy<CrestService> service;

    @Inject
    public FittingUseCaseImpl(final FittingFacade facade, final Lazy<CrestService> service) {
        this.facade = facade;
        this.service = service;
    }

    @Override
    public List<Fitting> loadFittings() {
        final List<Fitting> fittings = facade.list();
        if (null != service) {
            try {
                for (CrestFitting f: service.get().getFittings()) {
                    fittings.add(transform(f));
                }
            }
            catch (IllegalStateException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
        return fittings;
    }

    private static Fitting transform(final CrestFitting f) {
        final Fitting fitting = new Fitting();
        fitting.setId(f.getId());
        fitting.setDescription(f.getDescription());
        fitting.setName(f.getName());

        final CrestItem ship = f.getShip();
        if (null != ship) {
            fitting.setTypeID(ship.getId());
            fitting.setTypeName(ship.getName());
        }

        if ((null != f.getInventory()) && !f.getInventory().isEmpty()) {
            for (CrestInventory i: f.getInventory()) {
                final CrestItem item = i.getItem();
                if (null == item) {
                    LOG.error("null CREST item {}" + ToStringBuilder.reflectionToString(i));
                }
                else {
                    fitting.setItem(item.getName(), i.getFlag(), i.getQuantity());
                }
            }
        }
        return fitting;
    }
}
