import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

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
        FileArchive(Config.archiveFileName, archive);

        // MAIN ITERATIVE GENETIC EVOLUTION
        while (generation < engineParameters.getGenerations()) {

            generation++;

            // Mutate chromosomes
            ArrayList<Datum> mutants = mutate(chromosomes, engineParameters, monomerDefs);

            // Crossover chromosomes
            ArrayList<Datum> children = crossover(mutants, engineParameters, monomerDefs);

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
                    ArrayList<Datum> combined = new ArrayList<>();
                    ArrayList<Datum> toArchive = new ArrayList<>();
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
                            toArchive.add(children.get(i));
                        }
                    }
                    combined.sort(Comparator.comparingDouble(d -> d.score));
                    chromosomes.clear();
                    for (int i = 0; i < engineParameters.getPopulation(); i++) {
                        chromosomes.add(combined.get(i));
                    }
                    FileArchive(Config.archiveFileName, toArchive);
                    break;
                }
                default: {
                    Assertions.log("FATAL ERROR : GA ENGINE partent fate not set correctly in config.");
                    System.exit(1);
                }
            }

            // Introduce immigrants
            for (int i = engineParameters.getPopulation()-engineParameters.getImmigrantNumber();
                 i < engineParameters.getPopulation();
                 i++) { chromosomes.set(i, randomNewChromosome(monomerDefs)); }


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
    public ArrayList<Datum> mutate(ArrayList<Datum> individuals,
                                   GAEngineParameters engineParameters,
                                   MonomerDefs monomerDefs) {

        ArrayList<Datum> result = new ArrayList<>();
        Random random = new Random();

        switch (engineParameters.getMutationMethod()) {

            case "RANDOM": {
                for (int i = engineParameters.getMutationStart()-1; i < engineParameters.getMutationEnd()-1; i++) {
                    for (int gene = 0; gene < engineParameters.getGenes(); gene++) {
                        if (random.nextDouble() < engineParameters.getMutationFrequency1()) {
                            Datum chromosome = individuals.get(i);
                            // Get monomer number
                            int newMonomer = random.nextInt(monomerDefs.getSize()) + 1; // Don't want to get 0 as a number
                            int newNPFRNumber = monomerDefs.getNPFRMonomerNumber(newMonomer);
                            // Ooof, the next part is what you get for not making the data structure an array.
                            // Might want to make this a function?
                            switch (gene) {
                                case 0: {
                                    chromosome.a = newNPFRNumber;
                                    break;
                                }
                                case 1: {
                                    chromosome.b = newNPFRNumber;
                                    break;
                                }
                                case 2: {
                                    chromosome.c = newNPFRNumber;
                                    break;
                                }
                                case 3: {
                                    chromosome.d = newNPFRNumber;
                                    break;
                                }
                                case 4: {
                                    chromosome.e = 0; // Still only working with a max of four genes for the NPFR Library
                                    break;
                                }
                                default: {
                                    Assertions.log("FATAL ERROR : Sketchy gene number in mutate function of the GA Engine.");
                                    System.exit(1);
                                }
                            } // end of switch
                            // Meh, I can live with that for now.
                            individuals.set(i, chromosome);
                        }
                    }
                }
                // You have to update the return array :)
                result.addAll(individuals);
                break;
            }

            case "SIMILARITY": {
                for (int i = engineParameters.getMutationStart()-1; i < engineParameters.getMutationEnd()-1; i++) {
                    for (int gene = 0; gene < engineParameters.getGenes(); gene++) {
                        Double rDouble = random.nextDouble(); // We're only generating the random number once because we don't want
                                                              // the chance of skipping two monomers to be, for example, 0.05 * 0.02...
                        if (rDouble < engineParameters.getMutationFrequency1()) {
                            Datum chromosome = individuals.get(i);
                            // Assertions.log(chromosome.toString()); // DEBUG

                            // Are we going to offset by 1 or by 2 or -1 or -2?
                            int offset = 1;
                            if (random.nextDouble() < engineParameters.getMutationFrequency2()) { offset = 2; }
                            if (random.nextDouble() < 0.50) { offset = offset * -1; }
                            // Assertions.log("Chromosome " + i + " : gene " + gene + " : offset " + offset); // DEBUG

                            // What is the current index number of the NPFR number at that chromosome/gene
                            int currentNPFRNumber = -1;
                            switch (gene) {
                                case 0: { currentNPFRNumber = chromosome.a; break; }
                                case 1: { currentNPFRNumber = chromosome.b; break; }
                                case 2: { currentNPFRNumber = chromosome.c; break; }
                                case 3: { currentNPFRNumber = chromosome.d; break; }
                                case 4: { currentNPFRNumber = chromosome.e; break; }
                            }

                            // What is the index number in the monomer defs for that NPFR number
                            int currentIndex = monomerDefs.getIndexFromNPFRNumber(currentNPFRNumber);
                            // That number is zero-based...
                            //Assertions.log("Current NPFR number : " + currentNPFRNumber + " at index " + currentIndex + " in monomer defs."); // DEBUG

                            // Apply offset to index
                            currentIndex = currentIndex + offset; // yeah, making sure the next bit isn't fucked up

                            // Bounds checking... just going to roll it back in
                            if (currentIndex < 0) { currentIndex = 0; }
                            if (currentIndex > monomerDefs.getSize()-1) { currentIndex = monomerDefs.getSize()-1; }

                            // get the new NPFR number
                            int newNPFRNumber = -1;          // semaphore for shenanigans
                            newNPFRNumber = monomerDefs.getNPFRMonomerNumber(currentIndex+1);
                           //Assertions.log("New NPFR number : " + newNPFRNumber);  // DEBUG

                            // Ooof, the next part is what you get for not making the data structure an array.
                            // Might want to make this a function?
                            switch (gene) {
                                case 0: {
                                    chromosome.a = newNPFRNumber;
                                    break;
                                }
                                case 1: {
                                    chromosome.b = newNPFRNumber;
                                    break;
                                }
                                case 2: {
                                    chromosome.c = newNPFRNumber;
                                    break;
                                }
                                case 3: {
                                    chromosome.d = newNPFRNumber;
                                    break;
                                }
                                case 4: {
                                    chromosome.e = 0; // Still only working with a max of four genes for the NPFR Library
                                    break;
                                }
                                default: {
                                    Assertions.log("FATAL ERROR : Sketchy gene number in mutate function of the GA Engine.");
                                    System.exit(1);
                                }
                            } // end of switch
                            // Meh, I can live with that for now.
                            individuals.set(i, chromosome);
                           //  Assertions.log(chromosome.toString() + "\n");  // DEBUG
                        }
                    }
                }
                // You have to update the return array :)
                result.addAll(individuals);
                break;
            }

            default: {
                Assertions.log("FATAL ERROR : Something wrong with definition of mutation method.");
                System.exit(1);
            }

        } // end of method choice switch

        return result;
    }

    // *****************************************************************************************************************
    public ArrayList<Datum> crossover(ArrayList<Datum> individuals,
                                      GAEngineParameters engineParameters,
                                      MonomerDefs monomerDefs) {

        Random random = new Random();

        for (int i = engineParameters.getCrossoverStart(); i < engineParameters.getCrossoverEnd(); i = i + 2) {

            if (random.nextDouble() < engineParameters.getCrossoverFrequency()) {
                int crossOverPoint = random.nextInt(3);

                // OK, this bit is going to look really cludgy because we used letters for the chromosomes
                // Might want to switch to arrays in a rewrite
                // It's also problematic for mutatbility and deep/shallow copy issues. Thanks Java!

                Datum ch1 = individuals.get(i);
                Datum ch2 = individuals.get(i+1);

                switch (crossOverPoint) {

                    case 0: { int a = ch1.a; ch1.a = ch2.a; ch2.a = a; break; }

                    case 1: {
                        int a = ch1.a; ch1.a = ch2.a; ch2.a = a;
                        int b = ch1.b; ch1.b = ch2.b; ch2.b = b; break;
                    }

                    case 2: { int d = ch1.d; ch1.d = ch2.d; ch2.d = d; break;
                    }

                    default: { Assertions.log("FATAL ERROR : in GA crossover routine. It should not be possible to get here."); }

                } // End of switch on crossOverPoint

                individuals.set(i, ch1);
                individuals.set(i+1, ch2);
            }
        }

        return individuals;
    }

}





























