import com.marufeb.models.Cardinal;
import com.marufeb.models.Location;
import com.marufeb.models.SceneContext;
import com.marufeb.models.abstraction.Core;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.marufeb.models.Cardinal.*;

public class SaveTest {

    private static SceneContext context;

    @SuppressWarnings("all")
    private static void initContext() throws IOException {
        final File file = new File(System.getProperty("user.home") + File.separatorChar + "test");
        if (!file.exists())
            file.mkdirs();
        context = new SceneContext(file);
    }

    @BeforeClass
    public static void initialize() {
        try {
            initContext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void emptySaveTest() {
        assert context != null : "Initialization FAILED";

        System.out.println(context.getDataFolder());
        System.out.println(context.getLocations());
        System.out.println(context.getItems());
        System.out.println(context.getBackpackFile());
        context.getR().globalSave();
    }


    public void populateContext() {
        try {
            assert context != null : "Initialization FAILED";
            context.getMiscellaneous().add(context.createItem());
            context.getMiscellaneous().add(context.createItem());
            context.getMiscellaneous().add(context.createItem());
            context.getMiscellaneous().add(context.createLocation().setStarter(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void smallSaveTest() throws IOException {
        initContext();
        populateContext();
        context.getR().globalSave();
    }

    @Test
    public void locationsTest() {
        context.reload();
        final Location[] locations = context.getLocationsSet().toArray(Location[]::new);

        assert locations.length > 0 : "Locations FAILED to load properly";

        String one = getClass().getResource("/One.jpg").getFile();
        String two = getClass().getResource("/Two.jpg").getFile();
        String three = getClass().getResource("/Three.jpg").getFile();
        String four = getClass().getResource("/Four.jpg").getFile();

        System.out.println(one);
        System.out.println(two);
        System.out.println(three);
        System.out.println(four);

        System.out.println("Setting Cardinal points");

        System.out.println("Setting locations");
        locations[0].setByCardinal(N, one);
        locations[0].setByCardinal(E, two);
        locations[0].setByCardinal(S, three);
        locations[0].setByCardinal(W, four);

        context.createLocation(locations[0], N);

        System.out.println("Saving");
        context.miscellaneous.clear();
        context.getR().globalSave();
    }

    @Test
    public void loadTest() {
        context.reload();
        System.out.println("Loaded: "+ context.miscellaneous.size());
        final Map<String, List<Core>> collect = context.miscellaneous.stream().collect(Collectors.groupingBy(it -> it.getClass().getCanonicalName()));
        collect.forEach((key, value) -> System.out.println(key.concat("\t\t") + value.size()));
    }

}
