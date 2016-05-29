package com.tlabs.android.jeeves.model.data.sde;

import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.model.data.sde.entities.AgentEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.AttributeEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.BlueprintEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.CategoryEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.GroupEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.InventoryFlagEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemRequirementEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.JournalReferenceEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.LocationEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.MarketGroupEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.SkillEntity;
import com.tlabs.eve.api.Agent;
import com.tlabs.eve.api.Blueprint;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.CertificateTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EveFacadeImpl implements EveFacade {

    private final EveDatabase sde;

    public EveFacadeImpl(EveDatabase eve) {
        this.sde = eve;
    }

    @Override
    public Skill getSkill(final long skillID) {
        final SkillEntity entity = sde.getSkill(skillID);
        return null == entity ? null : EveEntities.transform(entity);
    }

    @Override
    public String getSkillName(final long skillID) {
        return sde.getSkillName(skillID);
    }

    @Override
    public List<Skill> getSkills(long groupID) {
        final List<SkillEntity> entities = sde.getSkills(groupID);
        final List<Skill> skills = new ArrayList<>(entities.size());
        for (SkillEntity e: entities) {
            skills.add(EveEntities.transform(e));
        }
        return skills;
    }

    @Override
    public Map<Long, String> getInventoryFlags() {
        final List<InventoryFlagEntity> flags = sde.getInventoryFlags();
        final Map<Long, String> m = new HashMap<>(flags.size());
        for (InventoryFlagEntity e: flags) {
            m.put(e.getId(), e.getText());
        }
        return m;
    }

    @Override
    public Map<Long, String> getReferenceTypes() {
        final List<JournalReferenceEntity> types = sde.getReferenceTypes();
        final Map<Long, String> m = new HashMap<>(types.size());
        for (JournalReferenceEntity e: types) {
            m.put(e.getId(), e.getName());
        }
        types.clear();
        return m;
    }

    @Override
    public Map<Long, String> getRegions() {
        final List<LocationEntity> entities = sde.getRegions();
        final Map<Long, String> regions = new HashMap<>(entities.size());
        for (LocationEntity e: entities) {
            regions.put(e.getId(), e.getName());
        }
        entities.clear();
        return regions;
    }

    @Override
    public String getLocationName(long locationId) {
        final LocationEntity entity = sde.getLocation(locationId);
        return null == entity ? null : entity.getName();
    }

    @Override
    public String getItemName(long itemID) {
        return sde.getItemName(itemID);
    }

    @Override
    public long getItemCategory(long itemID) {
        final ItemEntity entity = sde.getItem(itemID);
        return null == entity ? -1 : entity.getCategoryID();
    }

    @Override
    public Item getItem(long itemID) {
        final ItemEntity entity = sde.getItem(itemID);
        return null == entity ? null : EveEntities.transform(entity);
    }

    @Override
    public Item getItem(String itemName) {
        final ItemEntity entity = sde.getItem(itemName);
        return null == entity ? null : EveEntities.transform(entity);
    }

    @Override
    public List<Skill> getRequirements(long itemID) {
        final List<ItemRequirementEntity> requirements = sde.getRequirements(itemID);
        final List<Skill> skills = new ArrayList<>(requirements.size());
        for (ItemRequirementEntity r: requirements) {
            final Skill skill = getSkill(r.getRequiredTypeID());
            if (null == skill) {
                continue;
            }
            skill.setRank(r.getRequiredLevel());
            skills.add(skill);
        }
        return skills;
    }

    @Override
    public List<Item> getItemsRequiredFor(long itemID, int level) {
        final List<ItemEntity> entities = sde.getRequiredFor(itemID, level);
        final List<Item> items = new ArrayList<>(entities.size());
        for (ItemEntity e: entities) {
            items.add(EveEntities.transform(e));
        }
        entities.clear();
        return items;
    }

    @Override
    public Blueprint getBlueprint(long itemID) {
        final BlueprintEntity entity = sde.getBlueprint(itemID);
        if (null == entity) {
            return null;
        }
        return EveEntities.transform(entity);
    }

    @Override
    public Agent getAgent(long agentID) {
        final AgentEntity entity = sde.getAgent(agentID);
        if (null == entity) {
            return null;
        }
        return EveEntities.transform(entity);
    }

    @Override
    public CertificateTree getCertificates() {
        return EveEntities.transformCertificates(sde.getCertificates());
    }

    @Override
    public SkillTree getSkills() {
        return EveEntities.transformSkills(sde.getSkills());
    }

    @Override
    public Map<Long, String> getSkillGroups() {
        return sde.getSkillGroups();
    }

    @Override
    public String getCategoryName(long categoryId) {
        return sde.getCategoryName(categoryId);
    }

    @Override
    public Map<Long, String> getCategories() {
        final List<CategoryEntity> entities = sde.getCategories();
        final Map<Long, String> categories = new LinkedHashMap<>(entities.size());
        for (CategoryEntity e: entities) {
            categories.put(e.getCategoryID(), e.getCategoryName());
        }
        return categories;
    }

    @Override
    public Map<Long, String> getGroups(long categoryId) {
        final List<GroupEntity> entities = sde.getGroups(categoryId);
        final Map<Long, String> groups = new LinkedHashMap<>(entities.size());
        for (GroupEntity e: entities) {
            groups.put(e.getGroupID(), e.getGroupName());
        }
        return groups;
    }

    @Override
    public List<Item> getItems(long categoryId, long groupId) {
        final List<ItemEntity> entities = sde.getItems(categoryId, groupId);
        final List<Item> items = new ArrayList<>(entities.size());
        for (ItemEntity e: entities) {
            items.add(EveEntities.transform(e));
        }
        return items;
    }

    @Override
    public List<EveMarketGroup> getMarketGroups(long parentGroupId) {
        final List<MarketGroupEntity> entities = sde.getMarketGroups(parentGroupId);

        final List<EveMarketGroup> groups = new ArrayList<>(entities.size());
        for (MarketGroupEntity e: entities) {
            final EveMarketGroup group = EveEntities.transform(e);
            group.setChildCount(sde.countMarketChildren(group.getMarketGroupID()));
            //group.setItemIconID(sde.getMarketGroupIcon(group.getMarketGroupID()));
            //group.setItems(getMarketItems(group.getMarketGroupID()));
            groups.add(group);
        }
        return groups;
    }

    @Override
    public long getParentMarketGroup(long childGroupId, boolean top) {
        return sde.getParentMarketGroup(childGroupId, top);
    }

    @Override
    public List<Item> getMarketItems(long marketGroupId) {
        final List<ItemEntity> entities = sde.getMarketItems(marketGroupId);
        final List<Item> items = new ArrayList<>(entities.size());
        for (ItemEntity e: entities) {
            items.add(EveEntities.transform(e));
        }
        return items;
    }

    @Override
    public String getMarketGroupName(long marketGroupId) {
        String name =  sde.getMarketGroupName(marketGroupId);
        if (null == name) {
            name = Long.toString(marketGroupId);
        }
        return name;
    }

    @Override
    public List<ItemAttribute> getItemAttributes(long itemID) {
        final List<AttributeEntity> entities = sde.getItemAttributes(itemID);
        final List<ItemAttribute> attributes = new ArrayList<>(entities.size());
        for (AttributeEntity e: entities) {
            attributes.add(EveEntities.transform(e));
        }
        return attributes;
    }
}
