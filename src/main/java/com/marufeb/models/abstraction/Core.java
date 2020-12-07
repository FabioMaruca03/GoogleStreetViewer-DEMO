package com.marufeb.models.abstraction;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class takes care of representing the UUID for every Loadable class
 */
public abstract class Core implements Loadable, Serializable {
    protected UUID ID;

    /**
     * @return The UUID of the Core object
     */
    public UUID getID() {
        return ID;
    }
}
