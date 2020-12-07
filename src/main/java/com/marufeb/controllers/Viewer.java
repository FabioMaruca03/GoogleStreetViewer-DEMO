package com.marufeb.controllers;

import com.marufeb.App;
import com.marufeb.controllers.util.Type;
import com.marufeb.controllers.wrappers.ItemCollection;
import com.marufeb.controllers.wrappers.ItemWrapper;
import com.marufeb.controllers.wrappers.LocationWrapper;
import static com.marufeb.models.Cardinal.*;

import com.marufeb.models.Cardinal;
import com.marufeb.models.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Viewer implements Initializable {

    private LocationWrapper wrapper = null;
    private ItemCollection backpackWrapper;
    private ItemCollection contextItemsWrapper;

    private ObservableList<ItemWrapper> contextItems = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<ItemWrapper> backpackItems = FXCollections.observableList(new ArrayList<>());

    public static final Image NOT_FOUND_IMAGE = new Image(LocationWrapper.class.getResourceAsStream("/com/marufeb/unknown.jpg"));

    @FXML
    private BorderPane view;

    @FXML
    private HBox backpack;

    @FXML
    private VBox locationItems;

    @FXML
    private ImageView moveN;

    @FXML
    private ImageView moveW;

    @FXML
    private ImageView moveE;

    @FXML
    private ImageView moveS;

    private void updateControls(Cardinal current) {
        if (current == null) {
            moveN.setVisible(wrapper.getView(N) != NOT_FOUND_IMAGE);
            moveE.setVisible(wrapper.getView(E) != NOT_FOUND_IMAGE);
            moveS.setVisible(wrapper.getView(S) != NOT_FOUND_IMAGE);
            moveW.setVisible(wrapper.getView(W) != NOT_FOUND_IMAGE);
        } else {
            if (wrapper.getView(current)==Viewer.NOT_FOUND_IMAGE) {
                wrapper.setFacing(current.opposite());
                configureView(wrapper.getCurrentView());
            }
            switch (current) {
                case N: moveN.setVisible(wrapper.isTraversable(current)); break;
                case E: moveE.setVisible(wrapper.isTraversable(current)); break;
                case S: moveS.setVisible(wrapper.isTraversable(current)); break;
                case W: moveW.setVisible(wrapper.isTraversable(current)); break;
            }
        }
    }

    @FXML
    void moveE(MouseEvent event) {
        makeMovement(E);
        event.consume();
    }

    @FXML
    void moveN(MouseEvent event) {
        makeMovement(N);
        event.consume();
    }

    @FXML
    void moveS(MouseEvent event) {
        makeMovement(S);
        event.consume();
    }

    @FXML
    void moveW(MouseEvent event) {
        makeMovement(W);
        event.consume();
    }

    private void makeMovement(Cardinal cardinal) {
        boolean t = false;
        contextItems.removeIf(backpackItems::contains);
        if (wrapper.getFacing() == cardinal) {
            wrapper.into(cardinal, backpackWrapper);
        }
        else t = true;

        if (t || !wrapper.hasMoved()){
            wrapper.changePOV(cardinal);
        }

        if (wrapper.hasMoved()) {
            configureView(wrapper.getCurrentView());
            wrapper.changeItems(cardinal);
            contextItems = wrapper.getContextItems();;
            contextItemsWrapper = new ItemCollection(contextItems, Type.LOCATION_ITEMS);

            final Pane box = contextItemsWrapper.getBox();

            view.setRight(box);

            contextItemsWrapper.setFlex(backpackWrapper);
            backpackWrapper.setFlex(contextItemsWrapper);

            updateControls(null);
            updateControls(cardinal);
        }
    }

    private void configureView(Image image) {
        view.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final Optional<Location> start = App.global.getLocationsSet().stream().filter(Location::isStarter).findFirst();
        if (start.isEmpty())
            return;
        wrapper = new LocationWrapper(start.get());

        backpackItems.addAll(App.global.getBackpack().getItems().stream().map(ItemWrapper::new).collect(Collectors.toSet()));
        backpackWrapper = new ItemCollection(backpackItems, Type.BACKPACK);

        contextItems = wrapper.getContextItems();
        contextItemsWrapper = new ItemCollection(contextItems, Type.LOCATION_ITEMS);

        contextItemsWrapper.setFlex(backpackWrapper);
        backpackWrapper.setFlex(contextItemsWrapper);

        view.setTop(backpackWrapper.getBox());
        view.setRight(contextItemsWrapper.getBox());

        configureView(wrapper.getCurrentView());
        updateControls(null);
    }
}
