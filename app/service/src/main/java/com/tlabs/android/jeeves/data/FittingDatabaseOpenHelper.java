package com.tlabs.android.jeeves.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.fitting.FittingDatabase;

import java.sql.SQLException;

public final class FittingDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "fittings.db";

    private static final int V1 = 1;
    private static final int DATABASE_VERSION = V1;

    private static FittingDatabaseOpenHelper helper;

    private FittingDatabase database;

    private FittingDatabaseOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            this.database = new FittingDatabase(getConnectionSource());
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static FittingDatabaseOpenHelper from(final Context context) {
        if (null == helper) {
            helper = new FittingDatabaseOpenHelper(context.getApplicationContext());
        }
        return helper;
    }

    public FittingDatabase getDatabase() {
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
