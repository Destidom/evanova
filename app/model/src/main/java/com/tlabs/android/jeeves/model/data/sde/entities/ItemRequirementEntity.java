package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
/*SELECT
  Ships.typeName AS typeName,
  Ships.typeID AS typeID,
  Grouping.groupName AS typeGroupName,
  Grouping.groupID AS typeGroupID,
  Skills.typeName AS requiredTypeName,
  Skills.typeID as requiredTypeID,
  COALESCE(
      SkillLevel.valueInt,
      SkillLevel.valueFloat
  ) AS requiredLevel
FROM
  dgmTypeAttributes AS SkillName

  INNER JOIN invTypes AS Ships
    ON Ships.typeID = SkillName.typeID
  INNER JOIN invGroups AS Grouping
    ON Grouping.groupID = Ships.groupID
  INNER JOIN invTypes AS Skills
    ON (Skills.typeID = SkillName.valueInt OR Skills.typeID = SkillName.valueFloat)
       AND SkillName.attributeID IN (182, 183, 184, 1285, 1289, 1290)
  INNER JOIN dgmTypeAttributes AS SkillLevel
    ON SkillLevel.typeID = SkillName.typeID
       AND SkillLevel.attributeID IN (277, 278, 279, 1286, 1287, 1288)
WHERE

  Ships.published = 1 AND
  ((SkillName.attributeID = 182 AND
    SkillLevel.attributeID = 277) OR
   (SkillName.attributeID = 183 AND
    SkillLevel.attributeID = 278) OR
   (SkillName.attributeID = 184 AND
    SkillLevel.attributeID = 279) OR
   (SkillName.attributeID = 1285 AND
    SkillLevel.attributeID = 1286) OR
   (SkillName.attributeID = 1289 AND
    SkillLevel.attributeID = 1287) OR
   (SkillName.attributeID = 1290 AND
    SkillLevel.attributeID = 1288))*/
@Entity(name = "jeevesRequirements")
public class ItemRequirementEntity {

    @Column
    private String typeName;

    @Column
    private long typeID;

    @Column
    private String typeGroupName;

    @Column
    private long typeGroupID;

    @Column
    private String requiredTypeName;

    @Column
    private long requiredTypeID;

    @Column
    private int requiredLevel;

    public String getTypeName() {
        return typeName;
    }

    public long getTypeID() {
        return typeID;
    }

    public String getTypeGroupName() {
        return typeGroupName;
    }

    public long getTypeGroupID() {
        return typeGroupID;
    }

    public String getRequiredTypeName() {
        return requiredTypeName;
    }

    public long getRequiredTypeID() {
        return requiredTypeID;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }
}
