package com.tlabs.android.jeeves.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.sde.EveDatabase;
import com.tlabs.android.jeeves.service.R;
import com.tlabs.eve.dogma.FittingProvider;
import com.tlabs.eve.dogma.ormlite.OrmLiteFittingProvider;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public final class EveDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {
    private static final Logger LOG = LoggerFactory.getLogger(EveDatabaseOpenHelper.class);

    //the version we store in the helper.	When it changes, the helper is replaced from /res/raw.
    static final String DATABASE_VERSION = "citadel-1.0.0";

    static final String DATABASE_NAME = "eve.db";

    private static EveDatabaseOpenHelper helper;

    private EveDatabase database;
    private FittingProvider fitting;

    private EveDatabaseOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, 1);

        try {
            this.database = new EveDatabase(getConnectionSource());
            this.fitting = new OrmLiteFittingProvider(getConnectionSource());
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static EveDatabaseOpenHelper from(final Context context) {
        if (null != helper) {
            return helper;
        }
        final File db = context.getDatabasePath(DATABASE_NAME);
        try {
            if (db.exists()) {
                final SharedPreferences prefs = context.getSharedPreferences("EveDatabase", 0);
                final String version = prefs.getString("Version", null);
                if (DATABASE_VERSION.equals(version)) {
                    LOG.debug("EveDatabase#createInstance(): helper is up to date.");
                }
                else {
                    LOG.info("EveDatabase#createInstance(): upgrade from {} ", null == version ? "<none>" : version);
                    installEveDatabase(context);
                }
            }
            else {
                LOG.info("EveDatabase#createInstance(): installing new data.");
                installEveDatabase(context);
            }

        }
        catch (IOException e) {
            LOG.error("installInstance: unable to install EveDatabase: {} {}", e.getMessage(), e);
        }
        helper = new EveDatabaseOpenHelper(context);
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public FittingProvider getFitting() {
        return fitting;
    }

    public EveDatabase getDatabase() {
        return database;
    }

    private static boolean installEveDatabase(final Context context) throws IOException {
        final File db = context.getDatabasePath(DATABASE_NAME);
        if (db.exists() && !db.delete()) {
            throw new IOException("Cannot delete helper " + db);
        }
        db.getParentFile().mkdirs();
        db.createNewFile();

        InputStream in = new GzipCompressorInputStream(context.getResources().openRawResource(R.raw.jeeves));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(db));
        try {
            IOUtils.copy(in, out);
            out.flush();

            final SharedPreferences prefs = context.getSharedPreferences("EveDatabase", 0);
            prefs.edit().putString("Version", DATABASE_VERSION).apply();
            LOG.info("EveDatabase#installEveDatabase(): installed {} to {}", DATABASE_VERSION, db.toString());
        }
        finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return false;
    }

}
