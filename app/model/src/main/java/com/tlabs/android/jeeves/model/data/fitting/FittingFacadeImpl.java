package com.tlabs.android.jeeves.model.data.fitting;

import com.tlabs.android.jeeves.model.data.fitting.entities.FittingEntity;
import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.Fitting;
import com.tlabs.eve.dogma.FittingProvider;
import com.tlabs.eve.dogma.extra.format.FittingFormat;
import com.tlabs.eve.dogma.model.Item;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FittingFacadeImpl implements FittingFacade {
    private static final Logger LOG = LoggerFactory.getLogger(FittingFacadeImpl.class);
    interface Loader {

        List<Fitting> tryLoad(final InputStream in) throws IOException;
    }


    private final FittingDatabase fittings;
    private final FittingProvider eve;
    private final Fitter.Builder fitter;

    public FittingFacadeImpl(FittingDatabase database, FittingProvider eve) {
        this.eve = eve;
        this.fittings = database;

        this.fitter = new Fitter.Builder(eve);
    }

    @Override
    public List<Fitting> list() {
        final List<FittingEntity> fits = this.fittings.list();
        final List<Fitting> r = new ArrayList<>(fits.size());
        for (FittingEntity e: fits) {
            r.add(FittingEntities.transform(e));
        }
        return r;
    }

    @Override
    public Fitting load(long fittingID) {
        final FittingEntity f = this.fittings.load(fittingID);
        if (null == f) {
            return null;
        }

        return FittingEntities.transform(f);
    }

    @Override
    public boolean save(Fitting fitting) {
        final FittingEntity entity = FittingEntities.transform(fitting);
        if (null == entity) {
            return false;
        }
        if (this.fittings.save(entity)) {
            fitting.setId(entity.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long fittingID) {
        return this.fittings.delete(fittingID);
    }

    @Override
    public Fitting build(long itemID) {
        final Item item = this.eve.findItem(itemID);
        if (null == item) {
            return null;
        }
        return FittingEntities.transform(item);
    }


    @Override
    public List<Fitting> load(final InputStream in) {
        final List<Fitting> fits = loadImpl(in);
        final List<Fitting> returned = new ArrayList<>();

        for (Fitting f: fits) {
            final Item item = this.eve.findItem(f.getTypeName());
            if (null == item) {
                LOG.error("item type not found {}", f.getTypeName());
            }
            else {
                f.setTypeID(item.getItemID());
                returned.add(f);
            }
        }
        return returned;
    }

    @Override
    public Fitter fit(Fitting fitting) {
        return this.fitter.build(fitting);
    }

    @Override
    public boolean saveFittingOrder(List<Long> fittings) {
        return this.fittings.saveRanks(fittings) > 0;
    }

    private List<Fitting> loadImpl(InputStream in) {
        List<Fitting> r = loadImpl(in, new Loader() {
            @Override
            public List<Fitting> tryLoad(InputStream in) throws IOException {
                return FittingFormat.fromXML(in);
            }
        });
        if (null == r) {
            r = loadImpl(in, new Loader() {
                @Override
                public List<Fitting> tryLoad(InputStream in) throws IOException {
                    return FittingFormat.fromDNA(in);
                }
            });
        }
        if (null == r) {
            r = loadImpl(in, new Loader() {
                @Override
                public List<Fitting> tryLoad(InputStream in) throws IOException {
                    return FittingFormat.fromJSON(in);
                }
            });
        }
        if (null == r) {
            return Collections.emptyList();
        }
        return r;
    }

    private List<Fitting> loadImpl(final InputStream in, final Loader loader) {

        try {
            return loader.tryLoad(in);
        }
        catch (IOException e) {
            LOG.warn(e.getLocalizedMessage(), e);
            LOG.error(e.getLocalizedMessage());
            return null;
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }
}
