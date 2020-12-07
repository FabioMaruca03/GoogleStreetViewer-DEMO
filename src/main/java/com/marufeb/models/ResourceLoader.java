package com.marufeb.models;

import com.marufeb.models.abstraction.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class manages the load and unload procedure for each Model
 */
@SuppressWarnings("unchecked")
public class ResourceLoader {
    private SceneContext context;
    private static final ResourceLoader instance = new ResourceLoader();
    private final Logger logger = Logger.getGlobal();

    /**
     * Loads miscellaneous
     */
    private void globalLoad() throws ClassNotFoundException {
        if (context != null){
            Set<Core> cores = new HashSet<>();
            cores.addAll(loadFromFile(context.getItems()));
            cores.addAll(loadFromFile(context.getLocations()));
            cores.addAll(loadFromFile(context.getBackpackFile()));
            context.getMiscellaneous().addAll(cores);
            logger.info("Loading miscellaneous from ".concat(context.getDataFolder().getAbsolutePath()));
        } else logger.throwing("ResourceLoader","globalLoad", new NullPointerException("No context link! "));
    }

    /**
     * Saves miscellaneous
     */
    public void globalSave() {
        if (context != null) {
            logger.info("Saving miscellaneous");
            Set<Core> locations = new HashSet<>();
            Set<Core> backpack = new HashSet<>();
            Set<Core> items = new HashSet<>();
            context.getMiscellaneous().forEach(it -> {
                if (it instanceof Backpack) {
                    backpack.add(it);
                } else if (it instanceof Location) {
                    locations.add(it);
                } else if (it instanceof Item) {
                    items.add(it);
                }
            });
            try {
                save(backpack, context.getBackpackFile());
                save(locations, context.getLocations());
                save(items, context.getItems());
            } catch (IOException e) {
                logger.throwing("ResourceLoader", "globalSave", e);
            }
        } else logger.throwing("ResourceLoader","globalLoad", new NullPointerException("No context link! "));
    }

    /**
     * Loads data from file
     * @param from File that contains data
     * @return The stored Ser<T>
     * @throws ClassNotFoundException No class found
     */
    private Set<Core> loadFromFile(File from) throws ClassNotFoundException {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(from));
            return (Set<Core>) input.readObject();
        } catch (IOException e) {
            return new HashSet<>();
        } catch (Exception e) {
            logger.throwing("Resource Loader", "loadFromFile ".concat(from.getAbsolutePath()), e);
            throw e;
        }
    }

    /**
     * Saves a particular {@link Loadable} object
     * @param object The {@link Loadable} object you want to save
     * @param to The {@link File} you tant the object to be saved in
     * @param <T> The class of the object you want to save
     * @throws IOException If something goes wrong throws an exception
     */
    @SuppressWarnings("all")
    public <T extends Loadable> void save(Set<T> object, File to) throws IOException {
        if (!to.exists()) {
            if (new File(to.getParent()).mkdirs())
                logger.info("Making folder to save data: ".concat(to.getParent()));

            if (to.createNewFile())
                logger.info("Created new dataset file: ".concat(to.getAbsolutePath()));
            else {
                IOException e = new IOException("Cannot create the database file: ".concat(to.getAbsolutePath()));
                logger.throwing("Resource Loader", "saveToFile ".concat(to.getAbsolutePath()), e);
                throw e;
            }
        } else {
            to.delete();
            to.createNewFile();
        }
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(to))) {
            output.writeObject(object);
            logger.info("SAVED");
        } catch (Exception e) {
            logger.throwing("Resource Loader", "saveToFile ".concat(to.getAbsolutePath()), e);
            throw e;
        }
    }

    /**
     * Private constructor (See Singleton Design Pattern)
     */
    private ResourceLoader() { /* LAZY CONSTRUCTOR PATTERN */ }

    /**
     * The Singleton reference
     * @return The Singleton Instance. NULL if cannot load the {@link SceneContext}
     */
    public static ResourceLoader getInstance(SceneContext context) {
        try {
            instance.setContext(context);
        } catch (ClassNotFoundException e) {
            Logger.getGlobal().throwing("ResourceLoader", "getInstance", e);
            return null;
        }
        return instance;
    }

    /**
     * @return The current {@link SceneContext}
     */
    public SceneContext getContext() {
        return context;
    }

    /**
     * Sets the context of the instance as given. Then reloads miscellaneous.
     * @param context The {@link SceneContext} of the current Scene
     */
    public void setContext(SceneContext context) throws ClassNotFoundException {
        if (context!=null) {
            instance.context = context;
            instance.globalLoad();
            this.context = context;
        }
    }
}


