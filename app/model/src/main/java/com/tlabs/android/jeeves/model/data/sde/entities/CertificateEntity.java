package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "yamlCertificates")
public class CertificateEntity {

    @Id
    @Column(name = "_id")
    private long id;

    @Column
    private long groupID;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String recommendedFor;//id;id;etc.

    @Column
    private String skillTypes;//skillid:xxxxx (basic standard improved advanced elite);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendedFor() {
        return recommendedFor;
    }

    public void setRecommendedFor(String recommendedFor) {
        this.recommendedFor = recommendedFor;
    }

    public String getSkillTypes() {
        return skillTypes;
    }

    public void setSkillTypes(String skillTypes) {
        this.skillTypes = skillTypes;
    }
}
