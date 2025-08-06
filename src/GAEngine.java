import com.sun.source.tree.AssertTree;

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
            Datum aChromosome = randomNewChromosome(monomerDefs);
            chromosomes.add(aChromosome);
        }

        // Rank initial population
        chromosomes.sort(Comparator.comparingDouble(d -> d.score));

        Assertions.log("\nINITIAL POPULATION");
        for (int i = 0; i < chromosomes.size(); i++) {
            Assertions.log(chromosomes.get(i).toString());
        }
        Assertions.log("_________________________________________________________________");
        Assertions.log("\n");

        outputPopulation("GENERATION " + generation, chromosomes, engineParameters.getPopulation());


    }

    // Methods

    // *****************************************************************************************************************
    private Datum randomNewChromosome(MonomerDefs monomerDefs) {
        Random random = new Random();
        int a = random.nextInt(monomerDefs.getSize()) + 1;
        int b = random.nextInt(monomerDefs.getSize()) + 1;
        int c = random.nextInt(monomerDefs.getSize()) + 1;
        int d = random.nextInt(monomerDefs.getSize()) + 1;
        int e = 0;  // We're only going to do tetramers for now
        int npfrA = monomerDefs.getNPFRMonomerNumber(a);
        int npfrB = monomerDefs.getNPFRMonomerNumber(b);
        int npfrC = monomerDefs.getNPFRMonomerNumber(c);
        int npfrD = monomerDefs.getNPFRMonomerNumber(d);
        int npfrE = 0;
        double score = 1E10;
        try {
            score  = scorer.score(npfrA, npfrB, npfrC, npfrD, npfrE);
            // todo: maybe if the scorer returned the whole object not just the score?
        }
        catch (Exception exception) {
            Assertions.log("WARNING : No score calculated for [" + npfrA + "," + npfrB + "," + npfrC +
                    "," + npfrD + "," + npfrE + "]; Assigning 1e10 as score.");
        }
        System.out.println(npfrA + " " + npfrB + " " + npfrC + " " + npfrD + " " + npfrE);
        Datum aChromosome = new Datum(npfrA, npfrB, npfrC, npfrD, npfrE, score, 0.00, "-----");
        return aChromosome;
    }

    // *****************************************************************************************************************
    private void outputPopulation(String message, ArrayList<Datum> chromosomes, int numberToPrint) {
        Assertions.log(message);
        for (int i = 0; i < chromosomes.size(); i++) {
            Assertions.log(chromosomes.get(i).toString());
        }
        Assertions.log("_________________________________________________________________");
        Assertions.log("\n");
    }

    // *****************************************************************************************************************
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

    // *****************************************************************************************************************
    public ArrayList<Datum> mutate(ArrayList<Datum> individuals) {
        return individuals;
    }

    // *****************************************************************************************************************
    public ArrayList<Datum> crossover(ArrayList<Datum> individuals) {
        return individuals;
    }

    // *****************************************************************************************************************
    public boolean scoreExists(ArrayList<Datum> chrom, int a, int b, int c, int d, int e) {

        boolean isInList = false;

        for (int i = 0; i < chrom.size(); i++) {
            Datum test = chrom.get(i);
            if ((test.a == a) && (test.b == b) && (test.c == c) && (test.d == d) && (test.e ==e)) {
                isInList = true;
                break;
            }
        }

        return isInList;
    }

    // *****************************************************************************************************************
    public double retrieveScore(ArrayList<Datum> chrom, int a, int b, int c, int d, int e) {

        for (int i = 0; i < chrom.size(); i++) {
            Datum test = chrom.get(i);
            if ((test.a == a) && (test.b == b) && (test.c == c) && (test.d == d) && (test.e ==e)) {
                return test.score;
            }
        }
        return 1E10;
    }

    // *****************************************************************************************************************
    public boolean notInArray(ArrayList<Datum> chrom, Datum entry) {

        boolean isInArray = false;

        for (int i = 0; i < chrom.size(); i++) {
            Datum test = chrom.get(i);
            if ((test.a == entry.a) && (test.b == entry.b) && (test.c == entry.c) && (test.d == entry.d) && (test.e == entry.e)) {
                isInArray = true;
            }
        }
        return isInArray;
    }
}





























