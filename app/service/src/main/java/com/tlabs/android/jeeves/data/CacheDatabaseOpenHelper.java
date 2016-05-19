package com.tlabs.android.jeeves.data;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.cache.CacheDatabase;
import com.tlabs.android.util.Environment;

import java.io.File;
import java.sql.SQLException;

public final class CacheDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "cache.db";
    private static final int DATABASE_VERSION = 23;

    private static final class CacheContext extends ContextWrapper {
        final File cacheDirectory;

        public CacheContext(final Context context, final File cacheDirectory) {
            super(context);
            this.cacheDirectory = cacheDirectory;
        }

        @Override
        public boolean deleteDatabase(String name) {
            File file = getDatabasePath(name);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        }

        @Override
        public File getDatabasePath(String name) {
            return new File(cacheDirectory, name);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            if (!cacheDirectory.exists()) {
                if (!cacheDirectory.mkdirs()) {
                    throw new SQLiteException("Cannot create " + cacheDirectory);
                }
            }
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        }
    }

    private static CacheDatabaseOpenHelper instance;

    public static CacheDatabaseOpenHelper from(final Context context) {
        if (null == instance) {
            File cacheDirectory = Environment.getCacheDirectory(context);
            if (null == cacheDirectory) {
                cacheDirectory = Environment.getLocalStorageDirectory(context);
            }
            instance = new CacheDatabaseOpenHelper(new CacheContext(context, cacheDirectory), cacheDirectory);
        }
        return instance;
    }

    private final File cacheDirectory;
    private final CacheDatabase database;

    private CacheDatabaseOpenHelper(Context context, File cacheDirectory) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.cacheDirectory = new File(cacheDirectory + "/eveapi/");
        if (!this.cacheDirectory.exists()) {
            this.cacheDirectory.mkdirs();
        }

        try {
            this.database = new CacheDatabase(getConnectionSource(), cacheDirectory);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public CacheDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        this.database.onCreate(connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        this.database.onUpgrade(connectionSource, oldVersion, newVersion);
    }
}
