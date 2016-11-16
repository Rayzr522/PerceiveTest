package com.perceivedev.perceivetest;

import com.perceivedev.perceivecore.config.ConfigSerializable;

/**
 * @author Rayzr
 *
 */
public class EntityInfo implements ConfigSerializable {

    private double value;
    private String alias;

    public EntityInfo() {
    }

    public EntityInfo(double value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
