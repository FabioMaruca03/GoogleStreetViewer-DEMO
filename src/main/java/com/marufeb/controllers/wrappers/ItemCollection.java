package com.marufeb.controllers.wrappers;

import com.marufeb.controllers.util.Type;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemCollection{
    private final Pane box;
    private final Type type;
    private final ObservableList<ItemWrapper> items;

    public ItemCollection(ObservableList<ItemWrapper> items, Type type) {
        if (type == Type.BACKPACK) {
            final HBox hBox = new HBox();
            hBox.setSpacing(15);
            hBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(hBox, new Insets(10, 10, 10, 10));
            box = hBox;
        }
        else {
            final VBox vBox = new VBox();
            vBox.setSpacing(15);
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            VBox.setMargin(vBox, new Insets(10, 10, 10, 10));
            box = vBox;
        }
        this.type = type;
        this.items = items;
    }

    /**
     * Links to a particular {@link ItemCollection}
     * @param flex The collection to be flexed
     */
    public void setFlex(ItemCollection flex) {
        final ItemCollection back = type == Type.BACKPACK ? this : flex;
        final ItemCollection flexed = type == Type.BACKPACK ? flex : this;
        this.items.addListener((ListChangeListener<? super ItemWrapper>) change -> {
            System.out.println("Observed a change");
            change.next();
            box.getChildren()
                    .addAll(change.getList()
                            .stream()
                            .map(it->it.getBox(it.getImage()))
                            .filter(it->!box.getChildren().contains(it))
                            .collect(Collectors.toSet()));
            change.getList().forEach(it->ItemWrapper.flex(back, flexed, it.getBox(it.getImage()), change.getList().get(0), type));
        });

        final Stream<Pane> paneStream = items
                .stream()
                .map(it->it.getBox(it.getImage()));

        final List<Pane> panes = paneStream.collect(Collectors.toList());
        for (int i = 0; i < items.size(); i++) {
            ItemWrapper.flex(back, flexed, panes.get(i), items.get(i), type);
        }
        box.getChildren().clear();
        box.getChildren().addAll(panes);
    }

    public ObservableList<ItemWrapper> getItems() {
        return items;
    }

    public Pane getBox() {
        return box;
    }

    public Type getType() {
        return type;
    }
}
