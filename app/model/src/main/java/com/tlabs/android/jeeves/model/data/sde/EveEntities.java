package com.tlabs.android.jeeves.model.data.sde;


import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.model.data.sde.entities.AgentEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.AttributeEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.BlueprintEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.CertificateEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.ItemTraitEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.MarketGroupEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.NameEntity;
import com.tlabs.android.jeeves.model.data.sde.entities.SkillEntity;
import com.tlabs.eve.api.Agent;
import com.tlabs.eve.api.Blueprint;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.ItemTrait;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.Certificate;
import com.tlabs.eve.api.character.CertificateTree;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class EveEntities {
    private static final Logger LOG = LoggerFactory.getLogger(EveEntities.class);
    private EveEntities() {

    }

    public static Item transform(final ItemEntity entity) {
        final Item item = new Item();
        item.setItemID(entity.getTypeID());
        item.setName(entity.getTypeName());
        item.setDescription(entity.getDescription());
        item.setMass(entity.getMass());
        item.setVolume(entity.getVolume());
        item.setCapacity(entity.getCapacity());
        item.setBasePrice(entity.getBasePrice());
        item.setRaceID(entity.getRaceID());
        item.setGroupID(entity.getGroupID());
        item.setCategoryID(entity.getCategoryID());
        item.setMarketGroupID(entity.getMarketGroupID());
        item.setMetaGroupID(entity.getMetaGroupID());
        item.setPortion(entity.getPortionSize());
        item.setGroupName(entity.getGroupName());
        item.setCategoryName(entity.getCategoryName());
        item.setMarketGroupName(entity.getMarketGroupName());
        item.setMarketGroupDesciption(entity.getMarketGroupDescription());
        item.setMetaGroupName(entity.getMetaGroupName());
        try {
            final List<ItemTraitEntity> ite = new ArrayList<>(entity.getTraits());//ConcurrentModificationException?! WHAT?!
            final List<ItemTrait> traits = new ArrayList<>(ite.size());
            for (ItemTraitEntity e : ite) {
                traits.add(transform(e));
            }
            item.setTraits(traits);
        }
        //FIXME
        catch (ConcurrentModificationException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return item;
    }

    public static Skill transform(final SkillEntity entity) {
        final Skill skill = new Skill();
        skill.setSkillID(entity.getId());
        skill.setSkillName(entity.getName());
        skill.setGroupID(entity.getGroupID());
        skill.setGroupName(entity.getGroupName());
        skill.setRank(entity.getRank());
        skill.setPrimaryAttribute(entity.getPrimaryAttribute());
        skill.setSecondaryAttribute(entity.getSecondaryAttribute());
        skill.setDescription(entity.getDescription());
        skill.setPublished(true);

        addRequiredSkills(skill, entity.getRequiredSkills());
        return skill;
    }

    public static Blueprint transform(final BlueprintEntity entity) {
        final Blueprint bp = new Blueprint();
        bp.setTypeID(entity.getId());
        bp.setMaxProduction(entity.getProductionLimit());
        bp.setCopyingTime(entity.getCopyingTime());
        bp.setInventionTime(entity.getInventionTime());
        bp.setManufacturingTime(entity.getManufacturingTime());
        bp.setResearchMaterialsTime(entity.getResearchMaterialTime());
        bp.setResearchTimeTime(entity.getResearchTimeTime());
        return bp;
    }

    public static Agent transform(final AgentEntity entity) {
        final Agent agent = new Agent();
        agent.setTypeID(entity.getAgentTypeID());
        agent.setName(entity.getName());
        agent.setCorporationID(entity.getCorporationID());
        agent.setDivisionID(entity.getDivisionID());
        agent.setLevel(entity.getLevel());
        agent.setQuality(entity.getQuality());
        agent.setLocationID(entity.getLocationID());
        agent.setLocator(entity.getLocator());
        agent.setType(entity.getAgentType());
        //agent.setLocation(entity.getLocation().getName());
      //  agent.setCorporation();

        //agent.setDivisionName(entity.g);

        return agent;
    }

    public static SkillTree transformSkills(final List<SkillEntity> skills) {
        final SkillTree tree = new SkillTree();

        final Map<Long, SkillTree.SkillGroup> groups = new HashMap<>();
        for (SkillEntity e: skills) {
            SkillTree.SkillGroup group = groups.get(e.getGroupID());
            if (null == group) {
                group = new SkillTree.SkillGroup();
                group.setGroupID(e.getGroupID());
                group.setGroupName(e.getGroupName());
                groups.put(e.getGroupID(), group);
            }
            group.addSkill(transform(e));
        }
        return tree;
    }

    public static CertificateTree transformCertificates(final List<CertificateEntity> certificates) {
        final CertificateTree tree = new CertificateTree();
        for (CertificateEntity e: certificates) {
            tree.add(transform(e));
        }
        return tree;
    }

    public static Certificate transform(final CertificateEntity entity) {
        final Certificate c = new Certificate();
        c.setCertificateID(entity.getId());
        c.setGroupID(entity.getGroupID());
        c.setDescription(entity.getDescription());
        c.setName(entity.getName());

        final String[] recommended = StringUtils.split(entity.getRecommendedFor(), ";");
        if (ArrayUtils.isNotEmpty(recommended)) {
            final List<Long> r = new ArrayList<>(recommended.length);
            for (String id: recommended) {
                r.add(Long.parseLong(id));
            }
            c.setRecommendedFor(r);
        }

        addRequiredSkills(c, entity.getSkillTypes());
        return c;
    }

    public static ItemAttribute transform(final AttributeEntity entity) {
        final ItemAttribute attr = new ItemAttribute();
        attr.setID((int) entity.getAttributeID());
        attr.setName(entity.getDisplayName());

        attr.setCategoryID((int) entity.getCategoryID());
        attr.setCategoryName(entity.getCategoryName());

        if (null == entity.getValueFloat()) {
            if (null == entity.getValueInt()) {
                attr.setValue(entity.getDefaultValue());
            }
            else {
                attr.setValue(entity.getValueInt());
            }
        }
        else {
            attr.setValue(entity.getValueFloat());
        }
        return attr;
    }


    public static EveMarketGroup transform(final MarketGroupEntity entity) {
        final EveMarketGroup group = new EveMarketGroup();
        group.setIconID(entity.getIconID());
        group.setParentGroupID(null == entity.getParentGroupID() ? -1 : entity.getParentGroupID());
        group.setMarketGroupID(entity.getMarketGroupID());
        group.setMarketGroupName(entity.getMarketGroupName());
        return group;
    }

    private static void addRequiredSkills(final Certificate certificate, String required) {
        final Map<Long, Map<Certificate.Level, Integer>> skills = new LinkedHashMap<>();

        if (StringUtils.isNotBlank(required)) {
            final String[] chop = StringUtils.split(required, ";");
            if (null != chop) {
                for (String s : chop) {
                    String[] types = StringUtils.split(s, ":");
                    final long skillID = Long.parseLong(types[0]);
                    final Map<Certificate.Level, Integer> levelMap = new HashMap<>(5);

                    final String levels = types[1];
                    levelMap.put(Certificate.Level.BASIC, Integer.parseInt("" + levels.charAt(0)));
                    levelMap.put(Certificate.Level.STANDARD, Integer.parseInt("" + levels.charAt(1)));
                    levelMap.put(Certificate.Level.IMPROVED, Integer.parseInt("" + levels.charAt(2)));
                    levelMap.put(Certificate.Level.ADVANCED, Integer.parseInt("" + levels.charAt(3)));
                    levelMap.put(Certificate.Level.ELITE, Integer.parseInt("" + levels.charAt(4)));

                    skills.put(skillID, levelMap);
                }
            }
        }
        certificate.setRequiredSkills(skills);
    }

    private static void addRequiredSkills(final Skill skill, String required) {
        final String[] split = StringUtils.split(required, ";");
        if (ArrayUtils.isEmpty(split)) {
            return;
        }
        for (String s: split) {
            final String[] splat = StringUtils.split(s, "=");
            if (ArrayUtils.isNotEmpty(splat) && splat.length == 2) {
                skill.addRequiredSkill(Long.parseLong(splat[0]), Integer.parseInt(splat[1]));
            }
        }
    }

    private static ItemTrait transform(final ItemTraitEntity entity) {
        final ItemTrait trait = new ItemTrait();
        trait.setGroupID((int) entity.getNameID());
        trait.setBonus(entity.getBonus());
        trait.setRank(entity.getRank());
        trait.setUnitID((int) entity.getUnitID());
        trait.setUnitName(null == entity.getUnit() ? "" : entity.getUnit().getDisplay());
        trait.setText(entity.getText());

        final NameEntity name = entity.getName();
        if (null == name) {
            trait.setGroupName("General");
        }
        else {
            trait.setGroupName(name.getName());
        }
        return trait;
    }
}
