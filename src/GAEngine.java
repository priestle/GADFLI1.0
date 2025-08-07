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
        for (int i = 0; i < engineParameters.getPopulation(); i++) {
            Datum aChromosome = randomNewChromosome(monomerDefs);
            chromosomes.add(aChromosome);
        }

        // Rank initial population
        chromosomes.sort(Comparator.comparingDouble(d -> d.score));

        // Output population
        outputPopulation("GENERATION " + generation, chromosomes, engineParameters.getPopulation());

        // Archive initial population
        ArrayList<Datum> archive = new ArrayList<>();
        for (int i = 0; i < chromosomes.size(); i++) {
            archive.add(chromosomes.get(i));
        }

        // MAIN ITERATIVE GENETIC EVOLUTION
        while (generation < engineParameters.getGenerations()) {

            generation++;

            // Mutate chromosomes
            ArrayList<Datum> mutants = mutate(chromosomes);

            // Crossover chromosomes
            ArrayList<Datum> children = crossover(mutants);

            // Calculate new scores
            // todo: there should be something in here that short circuits recalculating a score
            for (int i = 0; i < children.size(); i++) {
                Datum test = children.get(i);
                try {
                    test.score = scorer.score(test.a, test.b, test.c, test.d, test.e);
                }
                catch (Exception e) {
                    Assertions.log("No score found for [" + test.a + "," + test.b + "," +
                            test.c + "," + test.d + "," + test.e + "]; assigning 1E10");
                }
                Datum result = new Datum(test.a, test.b, test.c, test.d, test.e, test.score, 0.00, "-----");
                children.set(i, result);
            }

            // Update the population
            switch (engineParameters.getParentFate()) {
                case "KILL": {
                    // All parents will be replaced with children
                    chromosomes.clear();
                    for (int i = 0; i < children.size(); i++) {
                        chromosomes.add(children.get(i));
                    }
                    break;
                }
                case "KEEP": {
                    // Parents and children will be mixed, the best will be kept, don't add duplicates
                    System.out.println("COMPETING PARENTS AND CHILDREN");
                    ArrayList<Datum> combined = new ArrayList<>();
                    for (int i = 0; i < chromosomes.size(); i++) {
                        combined.add(chromosomes.get(i));
                    }
                    for (int i = 0; i < children.size(); i++) {
                        boolean isInPopulation = false;
                        Datum test = children.get(i);
                        for (int j = 0; j < combined.size(); j++) {
                            Datum current = combined.get(j);
                            if (    (test.a == current.a) &&
                                    (test.b == current.b) &&
                                    (test.c == current.c) &&
                                    (test.d == current.d) &&
                                    (test.e == current.e)
                            ) {
                                isInPopulation = true;
                                break;
                            }
                        }
                        if (!isInPopulation) {
                            combined.add(children.get(i));
                        }
                    }
                    combined.sort(Comparator.comparingDouble(d -> d.score));
                    chromosomes.clear();
                    for (int i = 0; i < engineParameters.getPopulation(); i++) {
                        chromosomes.add(combined.get(i));
                    }
                    break;
                }
                default: {
                    Assertions.log("FATAL ERROR : GA ENGINE partent fate not set correctly in config.");
                    System.exit(1);
                }
            }

            // Introduce immigrants
            // todo: introduce immigrants

            // Output new population
            outputPopulation("GENERATION " + generation, chromosomes, engineParameters.getPopulation());
        }
        // END OF ITERATIVE GENETIC EVOLUTION

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

        ArrayList<Datum> result = new ArrayList<>();
        for (int i = 0; i < individuals.size(); i++) {
            result.add(individuals.get(i));
        }
        return result;
    }

    // *****************************************************************************************************************
    public ArrayList<Datum> crossover(ArrayList<Datum> individuals) {

        ArrayList<Datum> result = new ArrayList<>();
        for (int i = 0; i < individuals.size(); i++) {
            result.add(individuals.get(i));
        }
        return result;
    }

}





























