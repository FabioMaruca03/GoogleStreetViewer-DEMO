package com.marufeb.controllers.wrappers;

import com.marufeb.controllers.Viewer;
import com.marufeb.controllers.util.Type;
import com.marufeb.models.abstraction.Backpack;
import com.marufeb.models.abstraction.Item;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.io.FileInputStream;

public class ItemWrapper {
    private final Item item;
    private Pane box = new Pane();

    public ItemWrapper(Item item) {
        this.item = item;
    }

    public Image getImage() {
        try {
            return new Image(new FileInputStream(new File(item.getImage())));
        } catch (Exception e) {
            return Viewer.NOT_FOUND_IMAGE;
        }
    }

    public Pane getBox(Image image) {
        box.setPrefWidth(100);
        box.setPrefHeight(100);
        box.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        return box;
    }

    public static void flex(ItemCollection back, ItemCollection flexed, Pane pane, ItemWrapper item, Type type) {
        pane.setOnMouseClicked(it->{
            if (type == Type.LOCATION_ITEMS) {
                flexed.getItems().remove(item);
                back.getItems().removeIf(s->s==item);
                back.getItems().add(item);
            } else {
                back.getItems().remove(item);
                flexed.getItems().removeIf(s->s==item);
                flexed.getItems().add(item);
            }
            it.consume();
        });
    }

    public boolean drop() {
        return item.drop();
    }

    public boolean collect(Backpack backpack) {
        return item.collect(backpack);
    }

    public Item getItem() {
        return item;
    }
}
