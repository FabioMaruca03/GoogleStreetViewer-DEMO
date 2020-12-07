package com.marufeb.models.abstraction;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Abstract class representing a backpack
 */
public abstract class Backpack extends AbstractItem{

    protected int availableSpace = 0;
    protected List<Item> items = new LinkedList<>();
    protected Consumer<Backpack> usage;

    public Backpack(URI location) throws IOException {
        super(location);
    }

    public Backpack(URI location, Consumer<Backpack> usage) throws IOException {
        super(location);
        this.usage = usage;
    }

    public boolean addItem(Item item) {
        if (availableSpace <= items.size()+1) {
            item.collect(this);
            items.add(item);
            return true;
        } else return false;
    }

    public boolean remItem(Item item) {
        if (items.contains(item)) {
            item.drop();
            items.remove(item);
            return true;
        } else return false;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean collect(Backpack reference) {
        return false;
    }

    @Override
    public boolean drop() {
        return false;
    }
}
