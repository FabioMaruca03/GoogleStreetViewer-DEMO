package com.marufeb.models;

import com.marufeb.models.abstraction.Item;
import com.marufeb.models.abstraction.Usable;

import java.io.IOException;
import java.net.URI;

public class UsableItem extends Item implements Usable {

    public UsableItem(URI location) throws IOException {
        super(location);
    }

    @Override
    public boolean use() {
        return false;
    }
}
