package com.tlabs.android.jeeves.model.data.evanova;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tlabs.android.jeeves.model.data.evanova.entities.AccountEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ApiKeyEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ApiKeyOwnerEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ApiTokenEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.AssetEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterCloneEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterSkillEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractBidEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractItemEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CorporationEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.WalletEntity;
import com.tlabs.eve.api.character.SkillInTraining;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EvanovaDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(EvanovaDatabase.class);

    private static final String DATABASE_NAME = "evanova.db";//NEW NAME FIXME

    private static final int VERSION_V30 = 72;//Evanova 21 - history and indexes
    private static final int VERSION_V31 = 73;//Evanova 27 - sort order of corps and chars - old database - unpublished
    private static final int VERSION_V32 = 86;//Evanova 27 - ORM LITE
    private static final int VERSION_V33 = 89;//Evanova 31 - tokens

    public static final int DATABASE_VERSION = VERSION_V33;

    private ConnectionSource source;

    private Dao<AccountEntity, Long> accountDAO;
    private Dao<AssetEntity, Long> assetDAO;
    private Dao<ContractEntity, Long> contractDAO;
    private Dao<ContractItemEntity, Long> contractItemDAO;
    private Dao<ContractBidEntity, Long> contractBidDAO;
    private Dao<WalletEntity, Long> walletDAO;
    private Dao<CharacterEntity, Long> charDAO;
    private Dao<CharacterSkillEntity, Long> trainingDAO;
    private Dao<CharacterCloneEntity, Long> cloneDAO;
    private Dao<CorporationEntity, Long> corpDAO;

    public EvanovaDatabase(ConnectionSource source) throws SQLException {
        this.source = source;

        this.accountDAO = DaoManager.createDao(source, AccountEntity.class);
        this.assetDAO = DaoManager.createDao(source, AssetEntity.class);
        this.contractDAO = DaoManager.createDao(source, ContractEntity.class);
        this.contractItemDAO = DaoManager.createDao(source, ContractItemEntity.class);
        this.contractBidDAO = DaoManager.createDao(source, ContractBidEntity.class);
        this.walletDAO = DaoManager.createDao(source, WalletEntity.class);
        this.charDAO = DaoManager.createDao(source, CharacterEntity.class);
        this.trainingDAO = DaoManager.createDao(source, CharacterSkillEntity.class);
        this.cloneDAO = DaoManager.createDao(source, CharacterCloneEntity.class);
        this.corpDAO = DaoManager.createDao(source, CorporationEntity.class);
    }

    public void onCreate(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, AccountEntity.class);

            TableUtils.createTable(connectionSource, AssetEntity.class);
            TableUtils.createTable(connectionSource, ContractEntity.class);
            TableUtils.createTable(connectionSource, ContractItemEntity.class);
            TableUtils.createTable(connectionSource, ContractBidEntity.class);
            TableUtils.createTable(connectionSource, WalletEntity.class);

            TableUtils.createTable(connectionSource, CharacterEntity.class);
            TableUtils.createTable(connectionSource, CharacterSkillEntity.class);
            TableUtils.createTable(connectionSource, CharacterCloneEntity.class);

            TableUtils.createTable(connectionSource, CorporationEntity.class);

          //  EvanovaDatabaseMigration.migrateJeeves(context, database, connectionSource);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onUpgrade(ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //FIXME
       /* if (oldVersion == VERSION_V32) {
            try {
                EvanovaDatabaseMigration.migrateV32(connectionSource);
            }
            catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        else {
            onDelete(connectionSource);
            onCreate(connectionSource);
        }*/
    }

    public void onDelete(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, AccountEntity.class, true);

            TableUtils.dropTable(connectionSource, CorporationEntity.class, true);
            TableUtils.dropTable(connectionSource, CharacterCloneEntity.class, true);
            TableUtils.dropTable(connectionSource, CharacterSkillEntity.class, true);
            TableUtils.dropTable(connectionSource, CharacterEntity.class, true);

            TableUtils.dropTable(connectionSource, WalletEntity.class, true);
            TableUtils.dropTable(connectionSource, ContractBidEntity.class, true);
            TableUtils.dropTable(connectionSource, ContractItemEntity.class, true);
            TableUtils.dropTable(connectionSource, ContractEntity.class, true);

            TableUtils.dropTable(connectionSource, AssetEntity.class, true);

            TableUtils.dropTable(connectionSource, ApiTokenEntity.class, true);
            TableUtils.dropTable(connectionSource, ApiKeyOwnerEntity.class, true);
            TableUtils.dropTable(connectionSource, ApiKeyEntity.class, true);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Long> listAccounts() {
        try {
            final List<AccountEntity> entities = accountDAO.queryBuilder().selectColumns("id").query();
            final List<Long> returned = new ArrayList<>(entities.size());
            for (AccountEntity e: entities) {
                returned.add(e.getId());
            }
            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public AccountEntity getAccount(final long accountID) {
        try {
            return accountDAO.queryForId(accountID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public AccountEntity getAccountByOwner(final long ownerID) {
        try {
            return accountDAO.queryBuilder().where().eq("ownerID", ownerID).queryForFirst();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public int updateAccount(final AccountEntity entity) {
        try {
            final AccountEntity existing = accountDAO.queryBuilder().where().eq("ownerID", entity.getOwnerID()).queryForFirst();
            if (null == existing) {
                return accountDAO.create(entity);
            }

            entity.setId(existing.getId());
            entity.setSortRank(existing.getSortRank());

            if (entity.getKeyID() == 0) {
                entity.setKeyID(existing.getKeyID());
                entity.setKeyValue(existing.getKeyValue());
            }

            if (StringUtils.isBlank(entity.getRefreshToken())) {
                entity.setAccessToken(existing.getAccessToken());
                entity.setRefreshToken(existing.getRefreshToken());
            }

            if (entity.getMask() < existing.getMask()) {
                entity.setType(existing.getType());
                entity.setMask(existing.getMask());
            }

            return accountDAO.update(entity);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public int deleteAccount(final long id) {
        try {
            final AccountEntity entity = accountDAO.queryForId(id);
            if (null == entity) {
                return 0;
            }
            deleteCharacter(entity.getOwnerID());
            deleteCorporation(entity.getOwnerID());
            return accountDAO.deleteById(id);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public long getCorporationId(final long ownerID) {
        try {
            if (corpDAO.idExists(ownerID)) {
                return ownerID;
            }
            final CharacterEntity entity = charDAO.queryForId(ownerID);
            return null == entity ? -1 : entity.getCorporationID();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public String hitCharacter(final long charID) {
        try {
            final CharacterEntity e =
                    charDAO.queryBuilder().selectColumns("name").where().eq("characterID", charID).queryForFirst();
            return null == e ? null : e.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public CharacterEntity getCharacter(final long charID) {
        try {
            return charDAO.queryForId(charID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<CharacterCloneEntity> getCharacterClones(final long charID) {
        try {
            return cloneDAO.queryForEq("ownerID", charID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<CharacterSkillEntity> getCharacterSkills(final long charID) {
        try {
            final CharacterEntity e = charDAO.queryForId(charID);
            if (null == e) {
                return Collections.emptyList();
            }
            final List<CharacterSkillEntity> entities = e.getSkills();
            int i = 0;
            for (CharacterSkillEntity s: entities) {
                s.setOwnerID(charID);
                s.setPosition(i);
                i = i+ 1;
            }
            return entities;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<CharacterSkillEntity> getCharacterTraining(final long charID, final int status) {
        try {
            return trainingDAO
                    .queryBuilder()
                    .orderBy("position", true)
                    .where()
                    .eq("ownerID", charID)
                    .and()
                    .in("status", status)
                    .query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public int updateCharacter(final CharacterEntity entity) {
        try {
            final CharacterEntity existing = charDAO.queryForId(entity.getCharacterID());
            if (null == existing) {
                if (entity.getSortRank() == 0) {
                    entity.setSortRank((int)charDAO.countOf());
                }
                return charDAO.create(entity);
            }
            else {
                entity.setSortRank(existing.getSortRank());
                return charDAO.update(entity);
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public int updateCharacterClones(final long charID, final List<CharacterCloneEntity> entities) {
        try {
            deleteClones(charID);

            for (CharacterCloneEntity e: entities) {
                cloneDAO.create(e);
            }
            return entities.size();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public int updateCharacterSkills(final long charID, final List<CharacterSkillEntity> skills) {
        try {
            final CharacterEntity e = charDAO.queryForId(charID);
            if (null == e) {
                return 0;
            }
            e.setSkills(skills);
            return charDAO.update(e);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public int saveTrainingQueue(final long charID, final List<CharacterSkillEntity> entities) {
        return saveTrainingQueue(charID, entities, SkillInTraining.TYPE_QUEUE);
    }

    public int saveTrainingPlan(final long charID, final List<CharacterSkillEntity> entities) {
        return saveTrainingQueue(charID, entities, SkillInTraining.TYPE_PLAN);
    }

    private int saveTrainingQueue(final long charID, final List<CharacterSkillEntity> entities, final int trainingStatus) {
       // SQLiteDatabase db = getWritableDatabase();
     //   db.beginTransaction();
        try {
            final DeleteBuilder<CharacterSkillEntity, Long> delete = trainingDAO.deleteBuilder();
            delete.where().eq("ownerID", charID).and().eq("status", trainingStatus);
            delete.delete();

            for (CharacterSkillEntity e : entities) {
                e.setOwnerID(charID);
                e.setStatus(trainingStatus);
                trainingDAO.create(e);
            }
       //     db.setTransactionSuccessful();
            return entities.size();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    /*    finally {
            db.endTransaction();
        }*/
    }

    public void setCharacterSortOrder(final List<Long> ids) {
        try {
            int rank = 0;
            for (Long id: ids) {
                final CharacterEntity entity = charDAO.queryForId(id);
                if (null != entity) {
                    rank = rank + 1;
                    entity.setSortRank(rank);
                    charDAO.update(entity);
                }
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
    }

    public int deleteCharacter(final long charID) {
        //final SQLiteDatabase db = getWritableDatabase();
       // db.beginTransaction();
        try {
            deleteAssets(charID);
            deleteContracts(charID);
            deleteWallet(charID);
            deleteClones(charID);
            deleteSkills(charID);
            //deleteOwner(charID);//FIXME not sure about this

            final int r = charDAO.deleteById(charID);

         //   db.setTransactionSuccessful();
            return r;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
      /*  finally {
            db.endTransaction();
        }*/
    }

    public String hitCorporation(final long corpID) {
        try {
            final CorporationEntity e =
                    corpDAO.queryBuilder().selectColumns("name").where().eq("corporationID", corpID).queryForFirst();
            return null == e ? null : e.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public CorporationEntity getCorporation(final long corpID) {
        try {
            return corpDAO.queryForId(corpID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public int updateCorporation(final CorporationEntity entity) {
        try {
            final CorporationEntity existing = corpDAO.queryForId(entity.getCorporationID());
            if (null == existing) {
                entity.setSortRank((int) corpDAO.countOf());
                return corpDAO.create(entity);
            }
            else {
                entity.setSortRank(existing.getSortRank());
                return corpDAO.update(entity);
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public void setCorporationSortOrder(final List<Long> ids) {
        try {
            int rank = 0;
            for (Long id: ids) {
                final CorporationEntity entity = corpDAO.queryForId(id);
                if (null != entity) {
                    rank = rank + 1;
                    entity.setSortRank(rank);
                    corpDAO.update(entity);
                }
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
    }

    public int deleteCorporation(final long corpID) {
        //final SQLiteDatabase db = getWritableDatabase();
        //db.beginTransaction();
        try {
            deleteAssets(corpID);
            deleteContracts(corpID);
            deleteWallet(corpID);
            //deleteOwner(corpID);//FIXME not sure about this

            final int r = corpDAO.deleteById(corpID);

          //  db.setTransactionSuccessful();
            return r;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
        /*finally {
            db.endTransaction();
        }*/
    }

    public ContractEntity getContract(final long ownerID, final long contractID) {
        try {
            return contractDAO
                    .queryBuilder()
                    .where()
                    .eq("ownerID", ownerID)
                    .and()
                    .eq("contractID", contractID)
                    .queryForFirst();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<ContractEntity> getContracts(final long ownerID) {
        try {
            return contractDAO
                    .queryBuilder()
                    .where()
                    .eq("ownerID", ownerID).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> listContracts(final long ownerID, final long expiredAfter) {
        try {
            final Where<ContractEntity, Long> builder =
                    contractDAO.queryBuilder().selectColumns("contractID").where().eq("ownerID", ownerID);
            if (expiredAfter > 0) {
                builder.and().eq("dateCompleted", 0).and().le("dateExpired", expiredAfter);
            }

            final List<ContractEntity> entities = builder.query();
            final List<Long> returned = new ArrayList<>(entities.size());
            for (ContractEntity e: entities) {
                returned.add(e.getContractID());
            }
            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public int updateContracts(final long ownerID, final List<ContractEntity> contracts) {
        //final SQLiteDatabase db = getWritableDatabase();
      //  db.beginTransaction();
        try {
            deleteContracts(ownerID);

            int created = 0;
            for (ContractEntity e: contracts) {
                e.setOwnerID(ownerID);
                created = created + contractDAO.create(e);
            }
       //     db.setTransactionSuccessful();
            return created;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    /*    finally {
            db.endTransaction();
        }*/
    }

    public int updateContractBids(final long ownerID, final List<ContractBidEntity> bids) {
      //  final SQLiteDatabase db = getWritableDatabase();
      //  db.beginTransaction();
        try {
            for (ContractBidEntity e: bids) {
                e.setOwnerID(ownerID);
                contractBidDAO.createOrUpdate(e);
            }
     //       db.setTransactionSuccessful();
            return bids.size();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
     /*   finally {
            db.endTransaction();
        }*/
    }

    public int updateContractItems(final long ownerID, final List<ContractItemEntity> items) {
       // final SQLiteDatabase db = getWritableDatabase();
      //  db.beginTransaction();
        try {
            for (ContractItemEntity e: items) {
                e.setOwnerID(ownerID);
                contractItemDAO.createOrUpdate(e).getNumLinesChanged();
            }
       //     db.setTransactionSuccessful();
            return items.size();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
      /*  finally {
            db.endTransaction();
        }*/
    }

    public int updateCorporationWallet(final long corpID, final Map<Long, Double> balances) {
        try {
            final CorporationEntity entity = corpDAO.queryForId(corpID);
            if (null == entity) {
                return 0;
            }
            entity.setWalletBalances(balances);
            return corpDAO.update(entity);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public void saveAssets(final long ownerID, final List<AssetEntity> assets) {
       // final SQLiteDatabase db = getWritableDatabase();
       // db.beginTransaction();
        try {
            deleteAssets(ownerID);
            for (AssetEntity e: assets) {
                saveAsset(assetDAO, ownerID, e);
            }
      //      db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
     /*   finally {
            db.endTransaction();
        }*/
    }

    private void saveAsset(final Dao<AssetEntity, Long> assetDAO, final long ownerID, final AssetEntity asset) throws SQLException {
        asset.setOwnerID(ownerID);
        assetDAO.create(asset);
        for (AssetEntity child: asset.getAssets()) {
            child.setParentID(asset.getId());
            saveAsset(assetDAO, ownerID, child);
        }
    }

    public AssetEntity getAsset(final long ownerID, final long assetID) {
        try {
            return assetDAO.queryForId(assetID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<AssetEntity> getChildAsset(final long ownerID, final long assetID) {
        try {
            return assetDAO.queryBuilder()
                    .where()
                    .eq("parentID", assetID)
                    .and()
                    .eq("ownerID", ownerID)
                    .query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public Map<Long, Integer> getAssetGroups(long ownerID, String groupField, long filterTypeID) {
        try {
            final Map<Long, Integer> returned = new HashMap<>();

            final StringBuilder raw = new StringBuilder()
                .append("select distinct ")
                .append(groupField)
                .append(", count(")
                .append(groupField)
                .append(") from assets where ownerID = ")
                .append(ownerID);
            if (filterTypeID > 0) {
                raw.append(" and typeID = ");
                raw.append(filterTypeID);
            }
            raw.append(" group by ");
            raw.append(groupField);

            for (String[] result: assetDAO.queryRaw(raw.toString()).getResults()) {
                returned.put(Long.parseLong(result[0]), Integer.parseInt(result[1]));
            }

            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyMap();
        }
    }

    public List<AssetEntity> getAssetsByGroup(long ownerID, String groupField, long groupID, long filterTypeID) {
        try {
            Where<AssetEntity, Long> builder = assetDAO.queryBuilder()
                    .where()
                    .eq(groupField, groupID)
                    .and()
                    .eq("ownerID", ownerID);
            if (filterTypeID > 0) {
                builder.and().eq("typeID", filterTypeID);
            }
            return builder.query();

        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> listCharacters() {
        try {
            final List<CharacterEntity> list = charDAO.queryBuilder().selectColumns("characterID").query();
            final List<Long> returned = new ArrayList<>(list.size());
            for (CharacterEntity e: list) {
                returned.add(e.getCharacterID());
            }
            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> listCorporations() {
        try {
            final List<CorporationEntity> list = corpDAO.queryBuilder().selectColumns("corporationID").query();
            final List<Long> returned = new ArrayList<>(list.size());
            for (CorporationEntity e: list) {
                returned.add(e.getCorporationID());
            }
            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> listAccessMasks(final long ownerID) {
        try {
            final List<AccountEntity> accounts =
                    accountDAO.queryBuilder().selectColumns("mask").where().eq("ownerID", ownerID).query();
            final List<Long> returned = new ArrayList<>(accounts.size());
            for (AccountEntity e: accounts) {
                returned.add(e.getMask());
            }
            return returned;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    private int deleteAssets(final long ownerID) throws SQLException {
        final DeleteBuilder<AssetEntity, Long> delete = assetDAO.deleteBuilder();
        delete.where().eq("ownerID", ownerID);
        return delete.delete();
    }

    private int deleteWallet(final long ownerID) throws SQLException {
        final DeleteBuilder<WalletEntity, Long> delete = walletDAO.deleteBuilder();
        delete.where().eq("ownerID", ownerID);
        return delete.delete();
    }

    private int deleteContracts(final long ownerID) throws SQLException {
        final DeleteBuilder<ContractBidEntity, Long> deleteBid = contractBidDAO.deleteBuilder();
        deleteBid.where().eq("ownerID", ownerID);
        deleteBid.delete();

        final DeleteBuilder<ContractItemEntity, Long> deleteItem = contractItemDAO.deleteBuilder();
        deleteItem.where().eq("ownerID", ownerID);
        deleteItem.delete();

        final DeleteBuilder<ContractEntity, Long> deleteContract = contractDAO.deleteBuilder();
        deleteContract.where().eq("ownerID", ownerID);
        return deleteContract.delete();
    }

    private int deleteClones(final long ownerID) throws SQLException {
        final DeleteBuilder<CharacterCloneEntity, Long> delete = cloneDAO.deleteBuilder();
        delete.where().eq("ownerID", ownerID);
        return delete.delete();
    }

    private int deleteSkills(final long ownerID) throws SQLException {
        final DeleteBuilder<CharacterSkillEntity, Long> delete = trainingDAO.deleteBuilder();
        delete.where().eq("ownerID", ownerID);
        return delete.delete();
    }

    private int deleteOwner(final long ownerID) throws SQLException {
        DeleteBuilder<AccountEntity, Long> delete = accountDAO.deleteBuilder();
        delete.where().eq("ownerID", ownerID);
        return delete.delete();
    }
}
