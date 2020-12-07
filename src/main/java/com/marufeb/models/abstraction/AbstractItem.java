package com.marufeb.models.abstraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * The Item class represents a loadable item that can be used
 */
public abstract class AbstractItem extends Core implements Collectable, Droppable {

    protected File location;
    protected Backpack backpack = null;

    /**
     * Creates a new AbstractItem object
     * @param location The URI representing the FILE
     * @throws FileNotFoundException Thrown if the file won't be found
     */
    public AbstractItem(URI location) throws IOException {
        this(location, UUID.randomUUID());
    }

    /**
     * Creates a new AbstractItem object<br/>
     * <i>Use this constructor only when loading object from file</i>
     * @param location The URI representing the FILE
     * @param ID The UUID of the AbstractItem object.
     * @throws FileNotFoundException Thrown if the file won't be found
     */
    public AbstractItem(URI location, UUID ID) throws IOException {
        this.location = new File(Objects.requireNonNull(location));
        final String errorName = "Item [".concat(ID.toString()).concat("]");
        try {
            if (this.location.createNewFile())
            if (!this.location.exists()) {
                final FileNotFoundException e = new FileNotFoundException("Cannot find FILE: ".concat(this.location.getAbsolutePath()));
                Logger.getGlobal().throwing(errorName, "Second constructor", e);
                throw e;
            }
        } catch (IOException e) {
            Logger.getGlobal().throwing(errorName, "Second constructor", e);
            throw e;
        }
        this.ID = ID;
        Logger.getGlobal().info("Loaded Item: ".concat(ID.toString()));
    }

    /**
     * Unloads the entire AbstractItem
     * @return Always TRUE
     */
    @Override
    public boolean unload() {
        location = null;
        Logger.getGlobal().info("Unloaded AbstractItem ".concat(ID.toString()));
        return true;
    }
}
