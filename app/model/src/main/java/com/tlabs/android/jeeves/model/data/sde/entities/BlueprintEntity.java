package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "yamlBlueprints")
public class BlueprintEntity {

    @Id
    @Column(name = "_id")
    private long id;

    @Column
    private int productionLimit;

    @Column
    private long inventionTime;

    @Column
    private String inventionMaterials;//typeid=quantity;

    @Column
    private String inventionProducts;//typeid=quantity;

    @Column
    private String inventionSkills;//typeid=level;

    @Column
    private long manufacturingTime;

    @Column
    private String manufacturingMaterials;

    @Column
    private String manufacturingProducts;

    @Column
    private String manufacturingSkills;

    @Column
    private long copyingTime;

    @Column
    private String copyingMaterials;

    @Column
    private String copyingSkills;

    @Column
    private long researchMaterialTime;

    @Column
    private String researchMaterialMaterials;

    @Column
    private String researchMaterialSkills;

    @Column
    private long researchTimeTime;

    @Column
    private String researchTimeMaterials;

    @Column
    private String researchTimeSkills;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductionLimit() {
        return productionLimit;
    }

    public void setProductionLimit(int productionLimit) {
        this.productionLimit = productionLimit;
    }

    public long getInventionTime() {
        return inventionTime;
    }

    public void setInventionTime(long inventionTime) {
        this.inventionTime = inventionTime;
    }

    public String getInventionMaterials() {
        return inventionMaterials;
    }

    public void setInventionMaterials(String inventionMaterials) {
        this.inventionMaterials = inventionMaterials;
    }

    public String getInventionProducts() {
        return inventionProducts;
    }

    public void setInventionProducts(String inventionProducts) {
        this.inventionProducts = inventionProducts;
    }

    public String getInventionSkills() {
        return inventionSkills;
    }

    public void setInventionSkills(String inventionSkills) {
        this.inventionSkills = inventionSkills;
    }

    public long getManufacturingTime() {
        return manufacturingTime;
    }

    public void setManufacturingTime(long manufacturingTime) {
        this.manufacturingTime = manufacturingTime;
    }

    public String getManufacturingMaterials() {
        return manufacturingMaterials;
    }

    public void setManufacturingMaterials(String manufacturingMaterials) {
        this.manufacturingMaterials = manufacturingMaterials;
    }

    public String getManufacturingProducts() {
        return manufacturingProducts;
    }

    public void setManufacturingProducts(String manufacturingProducts) {
        this.manufacturingProducts = manufacturingProducts;
    }

    public String getManufacturingSkills() {
        return manufacturingSkills;
    }

    public void setManufacturingSkills(String manufacturingSkills) {
        this.manufacturingSkills = manufacturingSkills;
    }

    public long getCopyingTime() {
        return copyingTime;
    }

    public void setCopyingTime(long copyingTime) {
        this.copyingTime = copyingTime;
    }

    public String getCopyingMaterials() {
        return copyingMaterials;
    }

    public void setCopyingMaterials(String copyingMaterials) {
        this.copyingMaterials = copyingMaterials;
    }

    public String getCopyingSkills() {
        return copyingSkills;
    }

    public void setCopyingSkills(String copyingSkills) {
        this.copyingSkills = copyingSkills;
    }

    public long getResearchMaterialTime() {
        return researchMaterialTime;
    }

    public void setResearchMaterialTime(long researchMaterialTime) {
        this.researchMaterialTime = researchMaterialTime;
    }

    public String getResearchMaterialMaterials() {
        return researchMaterialMaterials;
    }

    public void setResearchMaterialMaterials(String researchMaterialMaterials) {
        this.researchMaterialMaterials = researchMaterialMaterials;
    }

    public String getResearchMaterialSkills() {
        return researchMaterialSkills;
    }

    public void setResearchMaterialSkills(String researchMaterialSkills) {
        this.researchMaterialSkills = researchMaterialSkills;
    }

    public long getResearchTimeTime() {
        return researchTimeTime;
    }

    public void setResearchTimeTime(long researchTimeTime) {
        this.researchTimeTime = researchTimeTime;
    }

    public String getResearchTimeMaterials() {
        return researchTimeMaterials;
    }

    public void setResearchTimeMaterials(String researchTimeMaterials) {
        this.researchTimeMaterials = researchTimeMaterials;
    }

    public String getResearchTimeSkills() {
        return researchTimeSkills;
    }

    public void setResearchTimeSkills(String researchTimeSkills) {
        this.researchTimeSkills = researchTimeSkills;
    }
}
