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
            int npfrA = monomerDefs.getNPFRMonomerNumber(a);
            int npfrB = monomerDefs.getNPFRMonomerNumber(b);
            int npfrC = monomerDefs.getNPFRMonomerNumber(c);
            int npfrD = monomerDefs.getNPFRMonomerNumber(d);
            int npfrE = 0;
            double score = scorer.score(npfrA, npfrB, npfrC, npfrD, npfrE);
            Datum aChromosome = new Datum(npfrA, npfrB, npfrC, npfrD, npfrE, score, 0.00, "-----");
            chromosomes.add(aChromosome);
        }

        // Rank initial population
        chromosomes.sort(Comparator.comparingDouble(d -> d.score));

        for (int i = 0; i < chromosomes.size(); i++) {
            System.out.println(chromosomes.get(i));
        }

        // Archive/Output initial population
        ArrayList<Datum> initialSetToArchive = new ArrayList<>();
        ArrayList<Datum> completeArchiveSet  = new ArrayList<>();

        for (int i = 0; i < chromosomes.size(); i++) {
            if (!initialSetToArchive.contains(chromosomes.get(i))) {
                initialSetToArchive.add(chromosomes.get(i));
                completeArchiveSet.add(chromosomes.get(i));
            }
        }
        String archiveFileName = Config.calculationRoot + "/" + engineParameters.getArchiveFileName();
        FileArchive(archiveFileName, initialSetToArchive);

        // While end condition not met
        while (generation < engineParameters.getGenerations()) {

            // Mutate population

            // Crossover population

            // Score population
                // Does score already exist?
                // Calculate new score

            // Rank population

            // Trim population if necessary

            // Archive new results

            // Output diagnostic results

            generation++;
        }
    }

    // Methods
    public void FileArchive(String archiveFileName, ArrayList<Datum> additions) {

        try (FileWriter outputF = new FileWriter(archiveFileName, true)) {
            for (int i = 0; i < additions.size(); i++) {
                String aS = additions.get(i).toCSV() + ",";
                outputF.write(aS + "\n");
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Cannot append calculation archive file.");
            System.exit(1);
        }

    }
}