package com.tlabs.android.jeeves.model.data.sde;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ReferenceObjectCache;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.sde.entities.AgentEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.AttributeEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.BlueprintEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.CategoryEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.CertificateEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.GroupEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.InventoryFlagEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemRequirementEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemTraitEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.JournalReferenceEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.LocationEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.MarketGroupEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.NameEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.SkillEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.UnitEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** You need the "data" project to build a suitable database.*/
public final class EveDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(EveDatabase.class);

    //the version we store in the database.	When it changes, the database is replaced from /res/raw.
    //static final String DATABASE_VERSION = "sde-20160429-TRANQUILITY";

   // static final String DATABASE_NAME = "eve.db";

    private Dao<NameEntity, Long> nameDAO;
    private Dao<LocationEntity, Long> locationDAO;

    private Dao<AgentEntity, Long> agentDAO;
    private Dao<UnitEntity, Long> unitDAO;

    private Dao<SkillEntity, Long> skillDAO;
    private Dao<CertificateEntity, Long> certificateDAO;

    private Dao<InventoryFlagEntity, Long> flagDAO;
    private Dao<JournalReferenceEntity, Long> journalDAO;
    private Dao<GroupEntity, Long> groupDAO;
    private Dao<MarketGroupEntity, Long> marketGroupDAO;

    private Dao<ItemEntity, Long> itemDAO;
    private Dao<ItemRequirementEntity, Long> requirementDAO;
    private Dao<ItemTraitEntity, Long> traitDAO;
    private Dao<BlueprintEntity, Long> blueprintDAO;

    private Dao<CategoryEntity, Long> categoryDAO;
    private Dao<AttributeEntity, Long> attributeDAO;

    public EveDatabase(final ConnectionSource source) throws SQLException {

        this.nameDAO = DaoManager.createDao(source, NameEntity.class);

        this.locationDAO = DaoManager.createDao(source, LocationEntity.class);
        this.locationDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.unitDAO = DaoManager.createDao(source, UnitEntity.class);
        this.unitDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.agentDAO = DaoManager.createDao(source, AgentEntity.class);
        this.agentDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.flagDAO = DaoManager.createDao(source, InventoryFlagEntity.class);
        this.flagDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.journalDAO = DaoManager.createDao(source, JournalReferenceEntity.class);
        this.journalDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.skillDAO = DaoManager.createDao(source, SkillEntity.class);
        this.skillDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.certificateDAO = DaoManager.createDao(source, CertificateEntity.class);
        this.certificateDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.groupDAO = DaoManager.createDao(source, GroupEntity.class);
        this.groupDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.marketGroupDAO = DaoManager.createDao(source, MarketGroupEntity.class);
        this.marketGroupDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.itemDAO = DaoManager.createDao(source, ItemEntity.class);
        this.itemDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.requirementDAO = DaoManager.createDao(source, ItemRequirementEntity.class);

        this.traitDAO = DaoManager.createDao(source, ItemTraitEntity.class);
        this.traitDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.blueprintDAO = DaoManager.createDao(source, BlueprintEntity.class);
        this.blueprintDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.categoryDAO = DaoManager.createDao(source, CategoryEntity.class);
        this.categoryDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());

        this.attributeDAO = DaoManager.createDao(source, AttributeEntity.class);
        this.attributeDAO.setObjectCache(ReferenceObjectCache.makeSoftCache());
    }

    public SkillEntity getSkill(final long skillID) {
        try {
            return skillDAO.queryForId(skillID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public String getSkillName(final long skillID) {
        try {
            final SkillEntity e = skillDAO.queryBuilder().selectColumns("name").where().eq("_id", skillID).queryForFirst();
            if (null == e) {
                return Long.toString(skillID);
            }
            return e.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Long.toString(skillID);
        }
    }

    public List<SkillEntity> getSkills(final long groupID) {
        try {
            return skillDAO.queryBuilder().orderBy("name", true).where().eq("groupID", groupID).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public BlueprintEntity getBlueprint(final long itemD) {
        try {
            return blueprintDAO.queryForId(itemD);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public AgentEntity getAgent(final long agentID) {
        try {
            return agentDAO.queryForId(agentID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public LocationEntity getLocation(final long locationID) {
        try {
            return locationDAO.queryForId(locationID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public String getItemName(final long itemID) {
        try {
            final NameEntity entity = nameDAO.queryBuilder()
                    .where()
                    .eq("name_id", itemID)
                    .and()
                    .eq("name_type", NameEntity.TYPE_ITEM)
                    .queryForFirst();
            return null == entity ? "" + itemID : entity.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ItemEntity getItem(final long itemID) {
        try {
            return fetch(itemDAO.queryForId(itemID));
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public ItemEntity getItem(final String itemName) {
        try {
            final List<ItemEntity> list = itemDAO.queryForEq("typeName", StringUtils.replace(itemName, "'", "''"));
            return list.size() == 0 ? null : fetch(list.get(0));
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<InventoryFlagEntity> getInventoryFlags() {
        try {
            return flagDAO.queryForAll();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<JournalReferenceEntity> getReferenceTypes() {
        try {
            return journalDAO.queryForAll();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<CertificateEntity> getCertificates() {
        try {
            return certificateDAO.queryForAll();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<SkillEntity> getSkills() {
        try {
            return skillDAO.queryForAll();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<LocationEntity> getRegions() {
        try {
            return locationDAO.queryBuilder().
                    orderBy("name", true).where().
                    eq("typeID", 3).and().
                    gt("_id", 10000000).and().
                    lt("_id", 11000000).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

   /* private static final String skillGroups =
            "SELECT count(_id) AS count, groupID, groupName " +
            "FROM apiSkillTree WHERE groupName NOT LIKE 'Fake Skills' " +
            "GROUP BY groupID ORDER BY groupName ASC";*/
    public Map<Long, String> getSkillGroups() {
        try {
            final List<SkillEntity> skills =
                    skillDAO.queryBuilder()
                    .distinct().selectColumns("groupId")
                    .selectColumns("groupID", "groupName")
                    .query();
            final Map<Long, String> map = new HashMap<>(skills.size());
            for (SkillEntity e: skills) {
                map.put(e.getGroupID(), e.getGroupName());
            }
            return map;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyMap();
        }
    }

    public String getMarketGroupName(long marketGroupID) {
        try {
            final MarketGroupEntity entity = marketGroupDAO
                    .queryBuilder()
                    .selectColumns("marketGroupName")
                    .where()
                    .eq("marketGroupID", marketGroupID)
                    .queryForFirst();
            return null == entity ? null : entity.getMarketGroupName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Long.toString(marketGroupID);
        }
    }

    public long getParentMarketGroup(long childGroupId, boolean top) {
        try {
            final MarketGroupEntity childGroup =
                    marketGroupDAO.queryBuilder()
                   .selectColumns("parentGroupID")
                   .where().eq("marketGroupID", childGroupId)
                   .queryForFirst();

            if (null == childGroup || null == childGroup.getParentGroupID() || childGroup.getParentGroupID() == 0) {
                return childGroupId;
            }
            if (top) {
                return getParentMarketGroup(childGroup.getParentGroupID(), true);
            }
            return childGroup.getParentGroupID();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public String getCategoryName(long categoryId) {
        try {
            final NameEntity name = nameDAO.queryBuilder().
                    where().eq("name_type", NameEntity.TYPE_CATEGORY).and().eq("name_id", categoryId).
                    queryForFirst();
            return null == name ? Long.toString(categoryId) : name.getName();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Long.toString(categoryId);
        }
    }

    public List<CategoryEntity> getCategories() {
        try {
            return categoryDAO.queryBuilder().
                    orderBy("categoryName", true).
                    where().
                    eq("published", 1).
                    query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<GroupEntity> getGroups(long categoryId) {
        try {
            return groupDAO.queryBuilder().
                    orderBy("groupName", true).
                    where().
                    eq("categoryID", categoryId).
                    and().
                    eq("published", 1).
                    query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public MarketGroupEntity getMarketGroup(long groupId) {
        try {
            return marketGroupDAO.queryForId(groupId);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<MarketGroupEntity> getMarketGroups(long parentGroupId) {
        try {
            if (parentGroupId < 0) {
                return marketGroupDAO.queryBuilder().
                        orderBy("marketGroupName", true).
                        where().
                        isNull("parentGroupID").
                        query();
            }
            return marketGroupDAO.queryBuilder().
                    orderBy("marketGroupName", true).
                    where().
                    eq("parentGroupID", parentGroupId).
                    query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public long countMarketChildren(long groupID) {
        try {
            return marketGroupDAO.queryBuilder().where().eq("parentGroupID",  groupID).countOf();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0l;
        }
    }

    public List<ItemEntity> getItems(long categoryId, long groupId) {
        try {
            return fetch(itemDAO.queryBuilder().
                    orderBy("typeName", true).
                    where().
                    eq("categoryID", categoryId).
                    and().
                    eq("groupID", groupId).
                    and().
                    eq("published", 1).
                    query());
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<ItemEntity> getMarketItems(long marketGroupId) {
        try {
            return fetch(itemDAO.queryBuilder().
                    orderBy("typeName", true).
                    where().
                    eq("marketGroupID", marketGroupId).
                    and().
                    eq("published", 1).
                    query());
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<ItemRequirementEntity> getRequirements(final long itemID) {
        try {
            return requirementDAO.queryForEq("typeID", itemID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public final List<ItemEntity> getRequiredFor(final long itemID, final int level) {
        try {
            final List<ItemRequirementEntity> requirements =
                    requirementDAO.queryBuilder().where().eq("requiredTypeID", itemID).and().eq("requiredLevel", level).query();
            final List<ItemEntity> items = new ArrayList<>(requirements.size());
            for (ItemRequirementEntity r: requirements) {
                items.add(getItem(r.getTypeID()));
            }
            return items;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<AttributeEntity> getItemAttributes(long itemID) {
        try {
            return attributeDAO.queryForEq("typeID", itemID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<ItemEntity> getItemsWithAttribute(final long attributeID) {
        try {
            QueryBuilder b = attributeDAO.queryBuilder().selectColumns("typeID");
            b.where().eq("attributeID", attributeID);

            return itemDAO.queryBuilder()
                    .where()
                    .in("typeID", b)
                    .query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    private ItemEntity fetch(final ItemEntity entity) throws SQLException {
        if (null == entity) {
            return null;
        }

        final List<ItemTraitEntity> traits = traitDAO.queryBuilder().where().eq("typeID", entity.getTypeID()).query();
        for (ItemTraitEntity t: traits) {
            t.setUnit(unitDAO.queryForId(t.getUnitID()));

            final NameEntity name =
                    nameDAO.queryBuilder().where().eq("name_type", 0).and().eq("name_id", t.getNameID()).queryForFirst();
            t.setName(name);
            entity.getTraits().add(t);
        }
        return entity;
    }

    private List<ItemEntity> fetch(final List<ItemEntity> entities) throws SQLException {
        for (ItemEntity e: entities) {
            fetch(e);
        }
        return entities;
    }
}
