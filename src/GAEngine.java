import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class GAEngine extends Engine {
    private final Scorer scorer;

    public GAEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running GA Engine...");

        Assertions.log("Retrieving GA engine parameters...");
        GAEngineParameters engineParameters = new GAEngineParameters(Config.calculationRoot + "/" + Config.engineConfigFileName);

        Assertions.log("Retrieving monomer definitions...");
        MonomerDefs monomerDefs = new MonomerDefs(Config.calculationRoot + "/" + Config.monomerDefinitionsFilename);

        if (Config.modelType.equals("NPFR")) {
            // todo: read in the sequence definition file to translate to NPFR numbers...
            // todo: this isn't needed by the other model types...
        }

        Assertions.log("\n");
        Assertions.AssertRunDateTime();
        Assertions.log("RUNNING GA ALGORITHM ON " + Config.modelType + " DATA...");

        Assertions.log("Generating initial population...\n");
        // Generate initial population

        int generation = 0;

        ArrayList<Datum> chromosomes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < engineParameters.getPopulation(); i++) {
            int numberOfMonomers = monomerDefs.getSize();
            int a = random.nextInt(numberOfMonomers) + 1;
            int b = random.nextInt(numberOfMonomers) + 1;
            int c = random.nextInt(numberOfMonomers) + 1;
            int d = random.nextInt(numberOfMonomers) + 1;
            int e = 0;
            double score = scorer.score(a, b, c, d, e);
            Datum aChromosome = new Datum(a, b, c, d, e, score, 0.00, "-----");
            chromosomes.add(aChromosome);
        }

        // Rank initial population
        chromosomes.sort(Comparator.comparingDouble(d -> d.score));

        for (int i = 0; i < chromosomes.size(); i++) {
            System.out.println(chromosomes.get(i));
        }

        // Archive/Output initial population
        ArrayList<Datum> archive = new ArrayList<>();
        for (int i = 0; i < chromosomes.size(); i++) {
            if (!archive.contains(chromosomes.get(i))) {
                archive.add(chromosomes.get(i));
            }
        }
        String archiveFileName = Config.calculationRoot + "/" + engineParameters.getArchiveFileName();
        FileArchive(archiveFileName, chromosomes, generation);

        // todo: a method to Assert the current population to the log files


        // While end condition not met

            // Mutate population

            // Crossover population

            // Score population

                // Does score already exist?

                // Calculate new score

                // Archive new results

            // Rank population

            // Output diagnostic results
    }

    // Methods
    public void FileArchive(String archiveFileName, ArrayList<Datum> additions, int generation) {

        try (FileWriter outputF = new FileWriter(archiveFileName, true)) {

        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Cannot append calculation archive file.");
            System.exit(1);
        }

    }
}