package com.marufeb.models;

import com.marufeb.models.abstraction.Backpack;
import com.marufeb.models.abstraction.Core;
import com.marufeb.models.abstraction.Item;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A Location representation (LAZY STRUCTURE)
 */
public class Location extends Core implements Cloneable{

    private final Map<Cardinal, Set<Item>> itemsMapper = new HashMap<>();
    private final Map<Cardinal, String> imageMapper = new HashMap<>();
    private final Map<Cardinal, Location> locationMapper = new HashMap<>();
    private boolean starter = false;

    /**
     * Default initialization
     */
    public Location() {
        super();
        ID = UUID.randomUUID();
        Logger.getGlobal().info("Init Location [ ".concat(ID.toString()).concat(" ]").concat("[START]"));
        itemsMapper.putIfAbsent(Cardinal.N, new HashSet<>());
        itemsMapper.putIfAbsent(Cardinal.E, new HashSet<>());
        itemsMapper.putIfAbsent(Cardinal.S, new HashSet<>());
        itemsMapper.putIfAbsent(Cardinal.W, new HashSet<>());

        imageMapper.putIfAbsent(Cardinal.N, null);
        imageMapper.putIfAbsent(Cardinal.E, null);
        imageMapper.putIfAbsent(Cardinal.S, null);
        imageMapper.putIfAbsent(Cardinal.W, null);

        locationMapper.putIfAbsent(Cardinal.N, null);
        locationMapper.putIfAbsent(Cardinal.E, null);
        locationMapper.putIfAbsent(Cardinal.S, null);
        locationMapper.putIfAbsent(Cardinal.W, null);
        Logger.getGlobal().info("Init Location [ ".concat(ID.toString()).concat(" ]").concat("[END]"));
    }

    /**
     * Modifies a Cardinal image of a particular {@link java.util.Map.Entry} as well as it's items ({@link Set})
     * @param cardinal The {@link Cardinal} that is going to be changed
     * @param items The new {@link Set} of {@link Item}
     */
    public void setByCardinal(Cardinal cardinal, Set<Item> items) {
        setByCardinal(cardinal);
        Logger.getGlobal().info("Altering items of ".concat(ID.toString()).concat(" [ ").concat(cardinal.name()).concat(" ]"));
        itemsMapper.put(cardinal, items);
    }

    /**
     * Modifies a Cardinal image of a particular {@link java.util.Map.Entry} as well as it's items ({@link Set})
     * @param cardinal The {@link Cardinal} that is going to be changed
     * @param image The new image path you want to modify
     */
    public void setByCardinal(Cardinal cardinal, String image) {
        setByCardinal(cardinal);
        Logger.getGlobal().info("Altering items of ".concat(ID.toString()).concat(" [ ").concat(cardinal.name()).concat(" ]"));
        imageMapper.put(cardinal, image);
    }

    /**
     * Modifies a Cardinal image of a particular {@link java.util.Map.Entry} as well as it's Location ({@link Location})
     * @param cardinal The {@link Cardinal} that is going to be changed
     * @param location The new {@link Location} of {@link Cardinal}
     */
    public void setByCardinal(Cardinal cardinal, Location location) {
        setByCardinal(cardinal);
        Logger.getGlobal().info("Altering Location of ".concat(ID.toString()).concat(" [ ").concat(cardinal.name()).concat(" ]"));
        locationMapper.put(cardinal, location);
    }

    /**
     * Modifies the Cardinal image of a particular {@link java.util.Map.Entry}
     * @param cardinal The {@link Cardinal} that is going to be changed
     */
    public void setByCardinal(Cardinal cardinal) {
        Logger.getGlobal().info("Altering the Cardinal of ".concat(ID.toString()).concat(" [ ").concat(cardinal.name()).concat(" ]"));
        final Cardinal key = itemsMapper.keySet()
                .stream()
                .filter(it -> it.name().equals(cardinal.name()))
                .collect(Collectors.toList()).get(0);
        final Set<Item> items = itemsMapper.get(key);
        itemsMapper.putIfAbsent(cardinal, items);
    }

    /**
     * @param cardinal The cardinal that you the image path
     * @return An {@link Optional} of String that may contains the image path (is empty if no image is set)
     */
    public Optional<String> getCurrentImage(Cardinal cardinal) {
        final String s = imageMapper.get(cardinal);
        return Optional.of(s==null?"":s);
    }

    public Set<Item> getCurrentItems(Cardinal cardinal) {
        return itemsMapper.get(cardinal);
    }

    /**
     * @param cardinal The cardinal you want to traverse
     * @return TRUE if is traversable otherwise FALSE
     */
    public boolean isTraversable(Cardinal cardinal) {
        return locationMapper.get(cardinal) != null;
    }

    /**
     * Gives you the chosen {@link Location} if it exists
     * @param cardinal The cardinal you want to enter
     * @return The resulting location if exists. If not returns null
     */
    public Location getLocationByCardinal(Cardinal cardinal) {
        return locationMapper.get(cardinal);
    }

    /**
     * Unloads the entire Location
     * @return Always TRUE
     */
    @Override
    public boolean unload() {
        itemsMapper.clear();
        locationMapper.clear();
        Logger.getGlobal().info("Unloaded Location ".concat(ID.toString()));
        return true;
    }

    public boolean isStarter() {
        return starter;
    }

    public Location setStarter(boolean starter) {
        this.starter = starter;
        return this;
    }

    @Override
    public Location clone() throws CloneNotSupportedException {
        Location clone = (Location) super.clone();
        clone.setByCardinal(Cardinal.N, itemsMapper.get(Cardinal.N));
        clone.setByCardinal(Cardinal.E, itemsMapper.get(Cardinal.E));
        clone.setByCardinal(Cardinal.S, itemsMapper.get(Cardinal.S));
        clone.setByCardinal(Cardinal.W, itemsMapper.get(Cardinal.W));
        return clone;
    }

}
