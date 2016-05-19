package com.tlabs.android.jeeves.model.data.fitting;

import com.tlabs.android.jeeves.model.data.fitting.entities.FittingEntity;
import com.tlabs.eve.dogma.Fitting;
import com.tlabs.eve.dogma.extra.format.FittingFormat;
import com.tlabs.eve.dogma.model.Item;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public final class FittingEntities {
    private static final Logger LOG = LoggerFactory.getLogger(FittingEntities.class);

    private FittingEntities() {}

    public static Fitting transform(final Item item) {
        final Fitting fitting = new Fitting();

        fitting.setTypeID(item.getItemID());
        fitting.setTypeName(item.getItemName());
        fitting.setName(item.getItemName());
        fitting.setDescription("");
        return fitting;
    }

    public static FittingEntity transform(final Fitting f) {
        final FittingEntity entity = new FittingEntity();
        entity.setId(f.getId());
        entity.setDescription(f.getDescription());
        entity.setName(f.getName());
        entity.setTypeID(f.getTypeID());
        entity.setTypeName(f.getTypeName());

        entity.setDna(FittingFormat.toXML(f));
        return entity;
    }

    public static Fitting transform(final FittingEntity f) {
        Fitting fitting = null;

        if (StringUtils.isNotBlank(f.getDna())) {
            fitting = mutate(f.getDna());
        }

        if (null == fitting) {
            fitting = new Fitting();
        }

        fitting.setTypeName(f.getTypeName());
        fitting.setTypeID(f.getTypeID());
        fitting.setName(f.getName());
        fitting.setDescription(f.getDescription());
        fitting.setId(f.getId());
        return fitting;
    }

    private static Fitting mutate(final String dna) {
        try {
            final List<Fitting> fit = FittingFormat.fromXML(new ByteArrayInputStream(dna.getBytes()));
            if (fit.size() == 0) {
                return null;
            }
            return fit.get(0);
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
