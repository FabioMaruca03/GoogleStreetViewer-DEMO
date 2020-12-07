package com.marufeb;

import com.marufeb.models.Location;
import com.marufeb.models.SceneContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static com.marufeb.models.Cardinal.*;
import static com.marufeb.models.Cardinal.W;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static SceneContext global = null;

    private static void initContext() throws IOException {
        final File file = new File(System.getProperty("user.home") + File.separatorChar + "test");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }

        } else file.delete();

        global = new SceneContext(file);
        global.getMiscellaneous().add(global.createLocation().setStarter(true));
        String one = App.class.getResource("/com/marufeb/start.png").getFile();
        String two = App.class.getResource("/com/marufeb/rightS.png").getFile();
        String three = App.class.getResource("/com/marufeb/backS.png").getFile();
        String four = App.class.getResource("/com/marufeb/leftS.png").getFile();
        String l2E = App.class.getResource("/com/marufeb/location2L.png").getFile();
        String l2S = App.class.getResource("/com/marufeb/location2S.png").getFile();
        String l3N = App.class.getResource("/com/marufeb/location3N.png").getFile();
        String l3S = App.class.getResource("/com/marufeb/location3S.png").getFile();

        String item = App.class.getResource("/com/marufeb/basket.png").getFile();

        Location[] locations = global.getLocationsSet().toArray(Location[]::new);

        System.out.println("Setting locations");

        locations[0].setByCardinal(N, one);
        locations[0].setByCardinal(E, two);
        locations[0].setByCardinal(S, three);
        locations[0].setByCardinal(W, four);


        global.createItems(locations[0], N, Set.of(item));

        final Location location2 = global.createLocation(locations[0], N);
        location2.setByCardinal(E, l2E);
        location2.setByCardinal(S, l2S);

        final Location location3 = global.createLocation(locations[0], W);
        location3.setByCardinal(W, l3N);
        location3.setByCardinal(S, l3S );

    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("viewer"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        initContext();
        launch();
    }

}