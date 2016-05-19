package com.tlabs.android.jeeves.model.data.cache;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheNameEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheRequestEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheSovereigntyEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheStationEntity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public final class CacheDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(CacheDatabase.class);

    private final File cacheDirectory;

    private Dao<CacheNameEntity, Long> nameDAO;
    private Dao<CacheStationEntity, Long> stationDAO;
    private Dao<CacheSovereigntyEntity, Long> sovDAO;
    private Dao<CacheRequestEntity, Long> requestDAO;

    public CacheDatabase(ConnectionSource source, File cacheDirectory) throws SQLException {
        this.cacheDirectory = new File(cacheDirectory + "/eveapi/");
        if (!this.cacheDirectory.exists()) {
            this.cacheDirectory.mkdirs();
        }

        this.nameDAO = DaoManager.createDao(source, CacheNameEntity.class);
        this.nameDAO.setObjectCache(new LruObjectCache(15));

        this.stationDAO = DaoManager.createDao(source, CacheStationEntity.class);
        this.stationDAO.setObjectCache(new LruObjectCache(15));

        this.sovDAO = DaoManager.createDao(source, CacheSovereigntyEntity.class);
        this.sovDAO.setObjectCache(new LruObjectCache(15));

        this.requestDAO = DaoManager.createDao(source, CacheRequestEntity.class);
        this.requestDAO.setObjectCache(new LruObjectCache(15));

    }

    public void onCreate(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, CacheNameEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CacheRequestEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CacheStationEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CacheSovereigntyEntity.class);
            this.cacheDirectory.mkdirs();
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onUpgrade(ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onDelete(connectionSource);
        onCreate(connectionSource);
    }

    public void onDelete(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, CacheNameEntity.class, true);
            TableUtils.dropTable(connectionSource, CacheRequestEntity.class, true);
            TableUtils.dropTable(connectionSource, CacheStationEntity.class, true);
            TableUtils.dropTable(connectionSource, CacheSovereigntyEntity.class, true);
            FileUtils.deleteQuietly(cacheDirectory);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getName(final long ownerID) {
        try {
            final CacheNameEntity entity = nameDAO.queryForId(ownerID);
            return null == entity ? null : entity.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public String getStationName(final long stationID) {
        try {
            final CacheStationEntity entity = stationDAO.queryForId(stationID);
            return null == entity ? null : entity.getStationName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public CacheRequestEntity getCached(final String key, final boolean returnExpired) {
        try {
            CacheRequestEntity entity = requestDAO.queryBuilder().where().eq("key", key).queryForFirst();
            if (null == entity) {
                LOG.debug("{}: cache miss", key);
                return null;
            }

            if (entity.getExpires() < System.currentTimeMillis()) {
                LOG.debug("{}: expires={}; now={}", key, new Date(entity.getExpires()), new Date(System.currentTimeMillis()));
                if (returnExpired) {
                    loadContent(entity);
                    return entity;
                }
                return null;
            }
            loadContent(entity);
            LOG.debug(
                    "{}: cache hit expires {} ({} mn)",
                    key, new Date(entity.getExpires()),
                    (entity.getExpires() - System.currentTimeMillis()) / 60000);
            return entity;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void cache(final CacheRequestEntity entity) {
        try {
            LOG.debug("{}: add until {}", entity.getKey(), new Date(entity.getExpires()));
            delete(entity.getKey());
            requestDAO.create(entity);
            saveContent(entity);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    private void delete(String key) {
        try {
            final DeleteBuilder<CacheRequestEntity, Long> delete = requestDAO.deleteBuilder();
            delete.where().eq("key", key);
            delete.delete();

            final File file = new File(this.cacheDirectory, key);
            if (file.exists()) {
                file.delete();
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void cacheStations(final Collection<CacheStationEntity> stations) {
        batch(stations, stationDAO);
    }

    public void cacheSovereignty(final Collection<CacheSovereigntyEntity> sovereignty) {
        batch(sovereignty, sovDAO);
    }

    public List<Long> filterExistingNames(final List<Long> from) {
        try {
            final List<CacheNameEntity> existing = nameDAO.queryBuilder()
                    .selectColumns("id")
                    .where().in("id", from)
                    .query();

            final List<Long> returned = new ArrayList<>();
            returned.addAll(from);

            for (CacheNameEntity e: existing) {
                returned.remove(e.getId());
            }
            LOG.debug("filterExistingNames {} out of {}", returned.size(), from.size());
            return returned;
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            return from;
        }
    }

    public void cacheNames(final Map<Long, String> names) {
        try {
            nameDAO.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Map.Entry<Long, String> n : names.entrySet()) {
                        final CacheNameEntity e = new CacheNameEntity();
                        e.setId(n.getKey());
                        e.setName(n.getValue());
                        nameDAO.createOrUpdate(e);
                    }
                    return null;
                }
            });
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void clear() {
        try {

            nameDAO.deleteBuilder().delete();
            stationDAO.deleteBuilder().delete();
            sovDAO.deleteBuilder().delete();
            requestDAO.deleteBuilder().delete();

            FileUtils.deleteQuietly(cacheDirectory);
            cacheDirectory.mkdirs();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    private <T> void batch(final Collection<T> entities, final Dao<T, Long> dao) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T e : entities) {
                        dao.createOrUpdate(e);
                    }
                    return null;
                }
            });
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    private void loadContent(final CacheRequestEntity entity) {
        final File file = new File(this.cacheDirectory, entity.getKey());
        if (!file.exists()) {
            LOG.warn("Cache file not found {}", file);
            return;
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            entity.setContent(IOUtils.toByteArray(in));
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    private void saveContent(final CacheRequestEntity entity) {
        final File file = new File(this.cacheDirectory, entity.getKey());
        if (file.exists()) {
            file.delete();
        }

        OutputStream out = null;
        try {
            file.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.write(entity.getContent(), out);
            out.flush();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        finally {
            IOUtils.closeQuietly(out);
        }
    }

}
