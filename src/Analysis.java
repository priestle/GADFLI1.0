import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Analysis {

    // Variables
    private int dafuq;

    // Constructor
    private Analysis() {}

    // Methods
    public static void run() {
        Assertions.log("RUNNING POST ANALYSIS...");

        /**
         *  The full calculation dump will be in the Config.archiveFile
         *  There are two broad types of calculation...
         *     Those we know the answer for the whole population (Random/Surface/Property)
         *     Those we only know the answers for the points we calculated (NPFR)
         */

        switch (Config.modelType) {

            case "PROPERTY": {
                PropertyAnalysis();
                break;
            }

            case "NPFR" : {
                break;
            }

            case "SURFACE": {
                break;
            }

            case "RANDOM": {
                break;
            }

        }
    }

    // Extra Methods

    private static void PropertyAnalysis() {

        Assertions.AssertRunDateTime();
        Assertions.log("ENGINE          : " + Config.engineType);
        Assertions.log("MODEL TYPE      : " + Config.modelType);
        Assertions.log("");
        Assertions.log("Population size : " + PropertyDataMetrics.N);
        Assertions.log("Minimum score   : " + PropertyDataMetrics.MIN);
        Assertions.log("Maximum score   : " + PropertyDataMetrics.MAX);
        Assertions.log("Mean            : " + PropertyDataMetrics.MEAN);
        Assertions.log("Stdev           : " + PropertyDataMetrics.STDEV);
        Assertions.log("");
        Assertions.log("Score threshold at 1.0 % : " + PropertyDataMetrics.THRESHOLD1_0);
        Assertions.log("                   0.3 % : " + PropertyDataMetrics.THRESHOLD0_3);
        Assertions.log("                   0.1 % : " + PropertyDataMetrics.THRESHOLD0_1);

        ArrayList<Datum> uniques = new ArrayList<>();   // De-duping the archive fileArray
        ArrayList<Datum> complete = new ArrayList<>();  // All the scores pulled from the archive file

        String archiveFileName = Config.calculationRoot + "/" + Config.archiveFileName;

        try (Scanner inputF = new Scanner(Paths.get(archiveFileName))) {
            while (inputF.hasNextLine()) {
                String row = inputF.nextLine();
                String[] parts = row.split(",");
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Archive file not found for data analysis.");
            System.exit(1);
        }

    }
}
