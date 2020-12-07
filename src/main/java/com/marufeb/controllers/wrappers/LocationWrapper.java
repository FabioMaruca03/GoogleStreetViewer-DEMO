package com.marufeb.controllers.wrappers;

import com.marufeb.controllers.Viewer;
import com.marufeb.models.Cardinal;
import com.marufeb.models.Location;
import com.marufeb.models.abstraction.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wraps in a specific Location
 * Usage:
 * <ul>
 *     <li>Single wrapper when playing</li>
 *     <li>Multiple wrappers to represent a wrapped static location</li>
 * </ul>
 */
public class LocationWrapper {
    private Location location;
    private Cardinal facing = Cardinal.N;
    private final ObservableList<ItemWrapper> contextItems = FXCollections.observableList(new ArrayList<>());
    private Location oldLocation = null;
    private boolean hasMoved = false;
    private boolean trans = false;

    /**
     * Default initialization. Wraps in a Location
     * @param location The location to be wrapped in
     */
    public LocationWrapper(Location location) {
        this.location = location;
        contextItems.addAll(getCurrentItems().stream().map(ItemWrapper::new).collect(Collectors.toSet()));
    }

    /**
     * Gives you the current {@link Image} (From JavaFX)
     * @return The actual {@link Image}
     */
    public Image getCurrentView() {
        return getView(facing);
    }

    /**
     * @param cardinal The cardinal you want the image
     * @return The cardinal's image
     */
    public Image getView(Cardinal cardinal) {
        final Optional<String> currentImage = location.getCurrentImage(cardinal);
        return currentImage
                .map(it-> {
                    try {
                        return new Image(new FileInputStream(new File(it)));
                    } catch (FileNotFoundException e) {
                        System.out.println("Cannot load image");
                        return Viewer.NOT_FOUND_IMAGE;
                    }
                })
                .orElseGet(() -> {
                    System.out.println("Cannot load image");
                    return Viewer.NOT_FOUND_IMAGE;
                });
    }

    /**
     * Checks if is traversable
     * @return TRUE is it's traversable otherwise FALSE
     */
    public boolean isTraversable(Cardinal position) {
        return location.isTraversable(position);
    }

    /**
     * Allows you to enter a location if that one exists
     * @param position The position you want to enter
     */
    public void into(Cardinal position, ItemCollection backpack) {
        if (location.isTraversable(position)) {
            if (oldLocation != null) {
                Location temp = this.oldLocation;
                this.oldLocation = this.location;
                this.location = temp;
                return;
            }
            this.location.setByCardinal(position, contextItems.stream().map(ItemWrapper::getItem).collect(Collectors.toSet()));
            oldLocation = this.location;
            this.location = location.getLocationByCardinal(position);
            hasMoved = true;
            trans = true;
        } else {
            hasMoved = false;
            trans = false;
        }
    }

    /**
     * Smart cast to {@link TreeItem}
     * @return The related {@link TreeItem}
     */
    public TreeItem<LocationWrapper> toTreeItem() {
        return new TreeItemLocation(this);
    }

    public void changePOV(Cardinal cardinal) {
        if (facing == cardinal)
            hasMoved = false;
        else {
            facing = cardinal;
            hasMoved = true;
        }
        trans = false;
    }

    public Set<Item> getCurrentItems() {
        return location.getCurrentItems(facing);
    }

    public void changeItems(Cardinal cardinal) {
        if (oldLocation != null) {
            oldLocation.setByCardinal(cardinal, contextItems.stream().map(ItemWrapper::getItem).collect(Collectors.toSet()));
        }

        this.contextItems.clear();
        this.contextItems.addAll(location.getCurrentItems(cardinal).stream().map(ItemWrapper::new).collect(Collectors.toList()));

        if (oldLocation != null) {
            this.contextItems.removeIf(it -> oldLocation.getCurrentItems(facing).stream().map(ItemWrapper::new).collect(Collectors.toSet()).contains(it));
        }

        location.setByCardinal(trans?cardinal.opposite():cardinal, contextItems.stream().map(ItemWrapper::getItem).collect(Collectors.toSet()));
    }

    /**
     * Represents a {@link TreeItem} of {@link LocationWrapper}
     */
    private static class TreeItemLocation extends TreeItem<LocationWrapper> {
        public TreeItemLocation(LocationWrapper locationWrapper) {
            super();
            setGraphic(new ImageView(locationWrapper.getCurrentView()));
        }
    }

    public ObservableList<ItemWrapper> getContextItems() {
        return contextItems;
    }

    public Cardinal getFacing() {
        return facing;
    }

    public void setFacing(Cardinal facing) {
        this.facing = facing;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}
