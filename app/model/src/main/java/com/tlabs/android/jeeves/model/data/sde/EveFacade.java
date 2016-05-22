package com.tlabs.android.jeeves.model.data.sde;


import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.eve.api.Agent;
import com.tlabs.eve.api.Blueprint;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CertificateTree;

import java.util.List;
import java.util.Map;

public interface EveFacade {
  //  String getName(final long nameId);

    Skill getSkill(final long skillID);

    String getSkillName(final long skillID);

    Map<Long, String> getInventoryFlags();

    Map<Long, String> getReferenceTypes();

    Map<Long, String> getRegions();

    String getLocationName(long locationId);

    String getItemName(final long itemID);

    long getItemCategory(final long itemID);

    Item getItem(final long itemID);

    Item getItem(final String itemName);

    List<Skill> getRequirements(final long itemID);

    List<Item> getItemsRequiredFor(final long itemID, final int level);

    Blueprint getBlueprint(final long itemID);

    Agent getAgent(final long agentID);

    CertificateTree getCertificates();

    Map<Long, String> getSkillGroups();

    List<Skill> getSkills(final long groupID);

    String getCategoryName(final long categoryId);

    Map<Long, String> getCategories();

    Map<Long, String> getGroups(final long categoryId);

    String getMarketGroupName(final long marketGroupId);

    List<EveMarketGroup> getMarketGroups(long parentGroupId);

    long getParentMarketGroup(long childGroupId, boolean top);

    List<Item> getMarketItems(final long marketGroupId);

    List<Item> getItems(final long categoryId, final long groupId);

    List<ItemAttribute> getItemAttributes(final long itemID);

  //  List<String> searchLocations(final String search);
}
