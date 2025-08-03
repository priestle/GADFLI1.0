import java.nio.file.ClosedFileSystemException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Data {

    /**
     * There are two general types of data. First, there is the NPFR compounds themselves which will
     * generate their own scores in the appropriate scoring classes. Second, there are surrogate functions
     * for quick optimizing and testing. These include 4D gaussian functions and precalculated NPFR compound
     * property data. These latter types of data also come with the precalculated answer (yes, speed!) and
     * also ranking data. Since these are precalculated funtions those answers are already known. Now, the property
     * search is going to be on a distance from some calculated function of mass, psa, and rotatable bonds. The exact
     * target will come from the config file and need to be calculated here.
     *
     * Data to be generated: NPFR (done elsewhere), SURFACE, PROPERTY, RANDOM
     */

    // Variables
    private static ArrayList<Datum> data = new ArrayList<>();


    // Constructor
    public Data() {}

    public static void init() {

        switch (Config.modelType) {

            case "NPFR":
                break; // Nothing to be done because this data is calculated via vina/gnina on the fly as needed

            case "SURFACE":
                initSurface();
                break;

            case "PROPERTY":
                initProperty();
                break;

            case "RANDOM":
                initRandom();
                break;

            default: {
                Assertions.log("FATAL ERROR : Cannot get the model type from the top config file.");
                System.exit(1);
            }
        }
    }

    // Gets
    public ArrayList<Datum> getData() {
        return data;
    }

    // Sets


    // Methods

    private static void initSurface() {
        Datum blah = new Datum(1,2,3,4,5,1.0, 2.0, "fred");
        data.add(blah);
    }

    private static void initRandom() {

    }

    // *****************************************************************************************************************
    private static void initProperty() {

        String configFileName = Config.calculationRoot + "/" + Config.scoringConfigFileName;

        Assertions.log("GADFLI " + Config.gadfliVersion);
        Assertions.log("MODEL NAME " + Config.modelName);
        Assertions.log("MODEL TYPE " + Config.modelType);
        Assertions.log("ENGINE " + Config.engineType);
        Assertions.AssertRunDateTime();
        Assertions.log("\n\n");

        Assertions.log("Bringing property data into the system...");

        String dataFileName = "";
        double targetMass   = 0.0;
        double targetPSA    = 0.0;
        int    rotbond      = -1;

        try (Scanner inputF = new Scanner(Paths.get(configFileName))) {

            while(inputF.hasNextLine()) {
                String row = inputF.nextLine();
                if (row.contains("DATA_FILENAME")) {
                    String[] parts = row.split(" +");
                    dataFileName = parts[1];
                }
                if (row.contains("TARGET_MASS")) {
                    String[] parts = row.split(" +");
                    targetMass = Double.parseDouble(parts[1]);
                }
                if (row.contains("TARGET_PSA")) {
                    String[] parts = row.split(" +");
                    targetPSA = Double.parseDouble(parts[1]);
                }
                if (row.contains("TARGET_ROTBOND")) {
                    String[] parts = row.split(" +");
                    rotbond = Integer.parseInt(parts[1]);
                }
            }

            // Sanity check on data construction
            if (dataFileName.isEmpty()) {
                Assertions.log("FATAL ERROR : Property data file not found or named correctly.");
                System.exit(1);
            }
            if (targetMass == 0.0) {
                Assertions.log("FATAL ERROR : No target mass found for property-based calculation.");
                System.exit(1);
            }
            if (targetPSA == 0.0) {
                Assertions.log("FATAL ERROR : No target PSA found for property-based calculations.");
                System.exit(1);
            }
            if (rotbond < 0 || rotbond > 100) {
                Assertions.log("FATAL ERROR : No rotatable bond target found or rotatable bonds request > 100");
                System.exit(1);
            }
            if (targetMass < 100 || targetMass > 2500) {
                Assertions.log("WARNING : Requested mass of " + targetMass + " makes little sense. Stopping calculation.");
                System.exit(1);
            }
            if (targetPSA < 0.01 || targetPSA > 10000) {
                Assertions.log("WARNING : Requested PSA of " + targetPSA + " makes little sense. Stopping calculation.");
                System.exit(1);
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Problem opening scorrer config file.");
            Assertions.log("\tSystems attempted to open " + configFileName);
            System.exit(1);
        }

        // Now read in the data from the file...
        try (Scanner inputF = new Scanner(Paths.get(dataFileName))) {
            while (inputF.hasNextLine()) {
                String row = inputF.nextLine();
                String[] parts = row.split(",");

                // Grab the point integers first
                String[] integerStrings = parts[1].split("\\.");
                int w = Integer.parseInt(integerStrings[0]);
                int x = Integer.parseInt(integerStrings[1]);
                int y = Integer.parseInt(integerStrings[2]);
                int z = Integer.parseInt(integerStrings[3]);

                // Grab the mass
                double mass = Double.parseDouble(parts[2]);
                double psa  = Double.parseDouble(parts[3]);
                int    rotb = Integer.parseInt(parts[4]);

                double distance = Math.pow(mass - targetMass,2) +
                                  Math.pow(psa - targetPSA, 2) +
                                  Math.pow(rotb - rotbond,2);
                distance = Math.pow(distance, 0.5);

                Datum myDatum = new Datum(w, x, y, z, 0, distance, 0.00, "-----");
                // That fifth integer being 0 is so we can expand it to a bigger NPFR later...

                data.add(myDatum);
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Issue with opening property data file name: " + dataFileName);
            Assertions.log("\t... or something in the file caused it to barf.");
            System.exit(1);
        }

        // Now we have to calculate the rank of each piece of data and find the global minimum
        Assertions.log("Calculating ranks and locating global minimum...");

        data.sort(Comparator.comparing(d -> d.score));

        for (int i = 0; i < data.size(); i++) {
            double rank = (double)i / data.size();
            data.get(i).rank = rank;
        }

        // DEBUG
        if (Config.debugStatus.equals("ON")) {
            for (int i = 0; i < 10; i++) {
                System.out.println(data.get(i));
            }
            for (int i = data.size() - 10; i < data.size(); i++) {
                System.out.println(data.get(i));
            }
        }
        // END DEBUG


    }
}
























