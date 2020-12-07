package com.marufeb.models;

import com.marufeb.models.abstraction.Backpack;

import java.io.IOException;
import java.net.URI;

/**
 * Represents a standard backpack
 */
public class DefaultBackpack extends Backpack {
    public DefaultBackpack(URI location) throws IOException {
        super(location);
        availableSpace = 5;
    }
}
