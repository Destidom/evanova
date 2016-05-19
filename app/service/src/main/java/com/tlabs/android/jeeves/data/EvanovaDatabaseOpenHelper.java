package com.tlabs.android.jeeves.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaDatabase;

import java.sql.SQLException;

public final class EvanovaDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "evanova.db";//NEW NAME FIXME

    private static final int VERSION_V30 = 72;//Evanova 21 - history and indexes
    private static final int VERSION_V31 = 73;//Evanova 27 - sort order of corps and chars - old database - unpublished
    private static final int VERSION_V32 = 86;//Evanova 27 - ORM LITE
    private static final int VERSION_V33 = 89;//Evanova 31 - tokens

    private static final int DATABASE_VERSION = VERSION_V33;
    private static EvanovaDatabaseOpenHelper helper;

    private EvanovaDatabase database;

    private EvanovaDatabaseOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            this.database = new EvanovaDatabase(getConnectionSource());
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static EvanovaDatabaseOpenHelper from(final Context context) {
        if (null == helper) {
            helper = new EvanovaDatabaseOpenHelper(context.getApplicationContext());
        }
        return helper;
    }

    public EvanovaDatabase getDatabase() {
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
