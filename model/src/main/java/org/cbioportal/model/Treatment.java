package org.cbioportal.model;

import java.io.Serializable;

public class Treatment implements Serializable {

    private int id;
    private int geneticEntityId;
    private String stableId;
    private String name;
    private String description;
    private String refLink;
   
    /**
     * @return the id
     */
    public int getInternalId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setInternalId(int id) {
        this.id = id;
    }

    /**
     * @return the geneticEntityId
     */
    public int getGeneticEntityId() {
        return geneticEntityId;
    }

    /**
     * @param geneticEntityId the geneticEntityId to set
     */
    public void setGeneticEntityId(Integer geneticEntityId) {
        this.geneticEntityId = geneticEntityId;
    }

    /**
     * @return the stableId
     */
    public String getStableId() {
        return stableId;
    }

    /**
     * @param stableId the stableId to set
     */
    public void setStableId(String stableId) {
        this.stableId = stableId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the refLink
     */
    public String getRefLink() {
        return refLink;
    }

    /**
     * @param refLink the refLink to set
     */
    public void setRefLink(String refLink) {
        this.refLink = refLink;
    }
}