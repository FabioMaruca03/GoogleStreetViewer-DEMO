package com.marufeb.controllers;

import com.marufeb.controllers.wrappers.LocationWrapper;
import com.marufeb.models.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Creator implements Initializable {

    @FXML
    private ImageView upImage;

    @FXML
    private ImageView rightImage;

    @FXML
    private ImageView leftImage;

    @FXML
    private ImageView downImage;

    @FXML
    private Parent sceneControls;

    @FXML
    private Parent scenePane;

    @FXML
    private TextField location;

    @FXML
    private TreeView<LocationWrapper> locationsTree;

    private SceneContext context;

    @FXML
    void loadScene(ActionEvent event) {
        try {
            if (location.getText().isBlank())
                return;
            context = new SceneContext(new File(location.getText()));
            scenePane.setDisable(false);
            sceneControls.setDisable(false);
            if (context.getLocationsSet().size() > 0)

            locationsTree.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.consume();
    }

    private void setRoot(TreeItem<LocationWrapper> location) {
        locationsTree.setRoot(location);
    }

    @FXML
    void addSubScene(ActionEvent event) {
        event.consume();
    }

    @FXML
    void newScene(ActionEvent event) {
        event.consume();
    }

    @FXML
    void removeSubScene(ActionEvent event) {
        event.consume();
    }

    @FXML
    void save(ActionEvent event) {
        event.consume();
    }

    @FXML
    void showImagePicker(MouseEvent event) {
        System.out.println("Pick");
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneControls.setDisable(true);
        scenePane.setDisable(true);
        locationsTree.setDisable(true);
    }
}
