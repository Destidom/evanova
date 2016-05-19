package com.tlabs.android.jeeves.model.data.evanova;

final class EvanovaDatabaseMigration {

    private static final String DATABASE_NAME = "jeeves.db";
    private static final String DATABASE_VERSION = "migration4";

  /*  public static void migrateJeeves(final Context context, final SQLiteDatabase toDatabase, final ConnectionSource toConnection) throws SQLException {
        toDatabase.beginTransaction();
        try {
            final File jeeves = context.getDatabasePath(DATABASE_NAME);
            if (jeeves.exists()) {
                migrateJeevesImpl(jeeves, toConnection);
            }
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        finally {
            toDatabase.endTransaction();
        }
    }

    public static void migrateV32(final ConnectionSource toConnection) throws SQLException {
        toDatabase.beginTransaction();
        try {
            migrateV32Impl(toConnection);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        finally {
            toDatabase.endTransaction();
        }
    }

    private static void migrateV32Impl(final ConnectionSource toConnection) throws SQLException {
        final Dao<AccountEntity, Long> accountDao = DaoManager.createDao(toConnection, AccountEntity.class);

        final Dao<ApiKeyEntity, Long> keysDao = DaoManager.createDao(toConnection, ApiKeyEntity.class);
        final Dao<ApiKeyOwnerEntity, Long> ownersDao = DaoManager.createDao(toConnection, ApiKeyOwnerEntity.class);

        for (ApiKeyEntity apiKey: keysDao.queryForAll()) {
            final List<ApiKeyOwnerEntity> owners = ownersDao.queryBuilder().where().eq("keyID", apiKey.getKeyID()).query();
            for (ApiKeyOwnerEntity owner : owners) {
                accountDao.create(transformAccount(owner.getOwnerID(), apiKey));
            }
        }

        TableUtils.dropTable(toConnection, ApiTokenEntity.class, true);
        TableUtils.dropTable(toConnection, ApiKeyOwnerEntity.class, true);
        TableUtils.dropTable(toConnection, ApiKeyEntity.class, true);
    }

    private static void migrateJeevesImpl(final File db, final ConnectionSource toConnection) throws SQLException {
        Log.i(LOG, "Migrating jeeves.db");
        final SQLiteDatabase from = SQLiteDatabase.openDatabase(db.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        try {
            doMigrateJeevesImpl(from, toConnection);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                SQLiteDatabase.deleteDatabase(db);
            }
        }
        finally {
            from.close();
        }
        db.delete();
        Log.i(LOG, "Migration completed");
    }

    private static void doMigrateJeevesImpl(final SQLiteDatabase fromDatabase, final ConnectionSource toConnection) throws SQLException {
        final Cursor keys = DatabaseHelper.queryTable(fromDatabase, "keys");
        if (keys.getCount() == 0) {
            keys.close();
            return;
        }

        final Dao<AccountEntity, Long> accountDao = DaoManager.createDao(toConnection, AccountEntity.class);
        keys.moveToFirst();
        while (!keys.isAfterLast()) {
            final Cursor owners = DatabaseHelper.rawQuery(
                    fromDatabase,
                    "SELECT ownerID from key_owners WHERE keyID = " + keys.getLong(keys.getColumnIndex("_id")));
            if (owners.getCount() > 0) {
                owners.moveToFirst();
                while (!owners.isAfterLast()) {
                    accountDao.create(transformJeevesApiKey(owners.getLong(0), keys));
                    owners.moveToNext();
                }
            }
            owners.close();
            keys.moveToNext();
        }
        keys.close();
    }

    private static AccountEntity transformAccount(final long ownerID, final ApiKeyEntity key) {
        final AccountEntity entity = new AccountEntity();
        entity.setOwnerID(ownerID);
        entity.setCreated(key.getCreated());
        entity.setExpires(key.getExpires());
        entity.setKeyID(key.getKeyID());
        entity.setKeyValue(key.getKeyValue());
        entity.setName(key.getKeyName());
        entity.setLogonCount(key.getLogonCount());
        entity.setLogonMinutes(key.getLogonMinutes());
        entity.setMask(entity.getMask());
        entity.setPaidUntil(entity.getPaidUntil());
        entity.setSortRank(key.getSortRank());
        entity.setStatus(key.getStatus());
        entity.setStatusMessage(key.getStatusMessage());
        entity.setType(key.getType());
        return entity;
    }

    private static AccountEntity transformJeevesApiKey(final long ownerID, final Cursor cursor) {
        final AccountEntity entity = new AccountEntity();
        entity.setOwnerID(ownerID);
        entity.setKeyID(cursor.getLong(cursor.getColumnIndex("_id")));
        entity.setName(cursor.getString(cursor.getColumnIndex("keyName")));
        entity.setKeyValue(cursor.getString(cursor.getColumnIndex("keyValue")));
        entity.setMask(cursor.getLong(cursor.getColumnIndex("mask")));
        entity.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        entity.setStatusMessage(cursor.getString(cursor.getColumnIndex("status_message")));
        entity.setCreated(cursor.getLong(cursor.getColumnIndex("created")));
        entity.setExpires(cursor.getLong(cursor.getColumnIndex("expires")));
        entity.setLogonMinutes(cursor.getLong(cursor.getColumnIndex("logon_minutes")));
        entity.setLogonCount(cursor.getLong(cursor.getColumnIndex("logon_count")));
        entity.setPaidUntil(cursor.getLong(cursor.getColumnIndex("paid_until")));
        entity.setType(cursor.getInt(cursor.getColumnIndex("type")));
        return entity;
    }*/

}
