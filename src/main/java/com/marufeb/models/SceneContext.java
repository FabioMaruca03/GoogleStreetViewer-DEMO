package com.marufeb.models;

import com.marufeb.models.abstraction.AbstractItem;
import com.marufeb.models.abstraction.Backpack;
import com.marufeb.models.abstraction.Core;
import com.marufeb.models.abstraction.Item;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Contains a scene context to load properly the entire context
 */
public class SceneContext {
    public LinkedHashSet<Core> miscellaneous = new LinkedHashSet<>();
    private final Logger logger = Logger.getGlobal();
    private final ResourceLoader R;
    private final Backpack backpack;
    private File dataFolder;
    private File locations;
    private File items;
    private File backpackFile;

    public SceneContext(File dataFolder) throws IOException {
        this.dataFolder = dataFolder;
        if (!this.dataFolder.exists()) {
            final FileNotFoundException e = new FileNotFoundException("Cannot find FILE: ".concat(this.dataFolder.getAbsolutePath()));
            logger.throwing("Folder [".concat(dataFolder.getAbsolutePath()).concat("]"), "Constructor", e);
            throw e;
        } else {
            locations = new File(dataFolder.toPath().resolve("locations.dat").toUri());
            items = new File(dataFolder.toPath().resolve("items.dat").toUri());
            backpackFile = new File(dataFolder.toPath().resolve("backpack.dat").toUri());
            R = ResourceLoader.getInstance(this);
            final List<Core> cores = miscellaneous.stream().filter(it -> it instanceof Backpack).collect(Collectors.toList());
            if (cores.isEmpty())
                backpack = new DefaultBackpack(backpackFile.toURI());
            else backpack = (DefaultBackpack) cores;
        }
    }

    /**
     * Loads a particular dataset
     * @param requirements The {@link Predicate} that used to filter the output
     * @param <T> The desired type of Element to load. It must extend {@link AbstractItem}
     * @return A {@link Set} of {@link AbstractItem} that match the given {@link Predicate}
     */
    @SuppressWarnings("unchecked")
    public <T extends Core> Set<T> load(Predicate<? super Core> requirements) {
        logger.info("Loading Abstract Items");
        final Set<T> collect = this.getMiscellaneous()
                .stream()
                .filter(Objects::nonNull)
                .filter(requirements)
                .map(it -> (T) it)
                .collect(Collectors.toSet());
        if (collect.size() > 0)
            collect.forEach(it->logger.info("[".concat(it.getID().toString()).concat("] - [".concat("TRUE").concat("]"))));
        else logger.warning("Cannot load any Abstract Item with that predicate");
        logger.info("Loaded all Abstract Items");
        return collect;
    }

    /**
     * Reloads the entire {@link SceneContext}
     */
    public void reload() {
        logger.info("Reloading SceneContext");
        if (R.getContext() != this)
            logger.warning("ResourceLoader was already set to ".concat(R.getContext().dataFolder.getAbsolutePath()));
        try {
            miscellaneous.clear();
            R.setContext(this);
        } catch (ClassNotFoundException e) {
            logger.throwing("SceneContext", "reload", e);
            return;
        }
        logger.info("Reloading done");
    }

    /**
     * Unloads every Core object and sets everything to null
     */
    private void unloadAll() {
        miscellaneous.forEach(Core::unload);
        miscellaneous.clear();
        locations = null;
        dataFolder = null;
        items = null;
        if (R.getContext() == this) {
            try {
                R.setContext(null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); // Cannot be reached
            }
        }
    }

    public LinkedHashSet<Core> getMiscellaneous() {
        return miscellaneous;
    }

    public File getLocations() {
        return locations;
    }

    public void setLocations(File locations) {
        this.locations = locations;
    }

    public File getItems() {
        return items;
    }

    public void setItems(File items) {
        this.items = items;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public File getBackpackFile() {
        return backpackFile;
    }

    public void setBackpackFile(File backpackFile) {
        this.backpackFile = backpackFile;
    }

    public Item createItem(String image) throws IOException {
        return new Item(items.toURI(), image);
    }

    public Item createItem() throws IOException {
        return new Item(items.toURI());
    }

    public Set<Item> createItems(Location parent, Cardinal position, Set<String> image) {
        final Set<Item> result = image.stream().map(it -> {
            try {
                final Item item = new Item(items.toURI());
                item.setImage(it);
                return item;
            } catch (IOException e) {
                return null;
            }
        }).collect(Collectors.toSet());
        parent.setByCardinal(position, result);
        return result;
    }

    public ResourceLoader getR() {
        return R;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    /**
     * @return A new Location
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Creates a new location from another location in a scene context
     * @param parent The {@link Location} that you're coming from
     * @param position The position that you're coming from
     * @return The resulting location
     */
    public Location createLocation(Location parent, Cardinal position) {
        final Location location = createLocation();
        location.setByCardinal(position.opposite(), parent);
        location.setByCardinal(position.opposite(), parent.getCurrentImage(position).orElse("/com/marufeb/unknown.jpg"));
        parent.setByCardinal(position, location);
        miscellaneous.add(location);
        return location;
    }

    /**
     * @return A {@link Location} set that contains all Location of a particular context
     */
    public Set<Location> getLocationsSet() {
        return load(it->it instanceof Location).stream().map(it->(Location) it).collect(Collectors.toSet());
    }
}
