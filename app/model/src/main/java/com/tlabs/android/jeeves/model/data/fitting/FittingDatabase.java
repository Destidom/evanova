package com.tlabs.android.jeeves.model.data.fitting;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tlabs.android.jeeves.model.data.fitting.entities.FittingEntity;
import com.tlabs.eve.dogma.Fitting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public final class FittingDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(FittingDatabase.class);

    private final Dao<FittingEntity, Long> fittingDAO;

    public FittingDatabase(final ConnectionSource source) throws SQLException {
        this.fittingDAO = DaoManager.createDao(source, FittingEntity.class);
    }
    
    public void onCreate(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FittingEntity.class);
            //migrateFittingsXML();
            if (this.fittingDAO.countOf() == 0) {
                save(defaultFitting());
            }
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onUpgrade(ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    public List<FittingEntity> list() {
        try {
            return fittingDAO.queryBuilder().orderBy("rank", true).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public FittingEntity load(final long id) {
        try {
            return fittingDAO.queryForId(id);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public boolean save(final FittingEntity fitting) {
        try {
            final FittingEntity existing = fittingDAO.queryForId(fitting.getId());
            if (null == existing) {
                fitting.setRank(fittingDAO.countOf());
                return fittingDAO.create(fitting) > 0;
            }

            fitting.setRank(existing.getRank());
            return fittingDAO.update(fitting) > 0;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean delete(final long id) {
        try {
            final DeleteBuilder<FittingEntity, Long> delete = fittingDAO.deleteBuilder();
            delete
                    .where()
                    .eq("_id", id);
            return delete.delete() > 0;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public int saveRanks(final List<Long> fittings) {
        try {
            int rank = 0;
            for (Long id: fittings) {
                final FittingEntity entity = fittingDAO.queryForId(id);
                if (null != entity) {
                    entity.setRank(rank);
                    fittingDAO.update(entity);
                    rank = rank + 1;
                }
            }
            return rank;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }
/*
    private void migrateFittingsXML() {
        final File file = new File(context.getFilesDir(), "fittings.xml");
        if (!file.exists()) {
            return;
        }
        InputStream in = null;
        try {
            final ContentResolver cr = context.getContentResolver();
            in = cr.openInputStream(Uri.fromFile(file));
            final List<Fitting> fittings = FittingFormat.fromXML(in);
            for (Fitting f: fittings) {
                f.setId(0);
                save(FittingEntities.transform(f));
            }
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        finally {
            IOUtils.closeQuietly(in);
            file.delete();
        }
    }*/

    private static FittingEntity defaultFitting() {
        final Fitting f = new Fitting();
        f.setName("Ibis One - Best Ibis");
        f.setDescription("provided by Evanova Android.");
        f.setTypeName("Ibis");
        f.setTypeID(601);

        f.setItem("Civilian Expanded Cargohold", Fitting.LOW1);
        f.setItem("Civilian Expanded Cargohold", Fitting.LOW2);
        f.setItem("1MN Civilian Afterburner", Fitting.MED1);
        f.setItem("Civilian EM Ward Field", Fitting.MED2);
        f.setItem("Civilian Light Missile Launcher", Fitting.HIGH1);
        f.setItem("Civilian Miner", Fitting.HIGH2);

        return FittingEntities.transform(f);
    }
}
