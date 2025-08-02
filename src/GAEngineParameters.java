import java.nio.file.Paths;
import java.util.Scanner;

public class GAEngineParameters {

    // Variables
    private int    generations        = 0;
    private int    population         = 0;
    private int    genes              = 0;
    private double mutationFrequency1 = -1.0;
    private double mutationFrequency2 = -1.0;
    private String mutationMethod     = "";
    private String parentFate         = "";
    private int    mutationStart      = -1;
    private int    mutationEnd        = -1;
    private double crossoverFrequency = -1.0;
    private int    crossoverStart     = -1;
    private int    crossoverEnd       = -1;

    // Constructor
    public GAEngineParameters(String configFileName) {

        try (Scanner inputF = new Scanner(Paths.get(configFileName))) {
            while (inputF.hasNextLine()) {
                String row = inputF.nextLine();

                if (row.contains("GENERATIONS")) {
                    this.generations = Integer.parseInt(secondString(row));
                }

                if (row.contains("POPULATION")) {
                    this.population = Integer.parseInt(secondString(row));
                }

                if (row.contains("GENES")) {
                    this.genes = Integer.parseInt(secondString(row));
                }

                if (row.contains("MUTATION_FREQUENCY_1")) {
                    this.mutationFrequency1 = Double.parseDouble(secondString(row));
                }

                if (row.contains("MUTATION_FREQUENCY_2")) {
                    this.mutationFrequency2 = Double.parseDouble(secondString(row));
                }

                if (row.contains("PARENT_FATE")) {
                    this.parentFate = secondString(row);
                }

                if (row.contains("MUTATION_START")) {
                    this.mutationStart = Integer.parseInt(secondString(row));
                }

                if (row.contains("MUTATION_END")) {
                    this.mutationEnd = Integer.parseInt(secondString(row));
                }

                if (row.contains("CROSSOVER_FREQUENCY")) {
                    this.crossoverFrequency = Double.parseDouble(secondString(row));
                }

                if (row.contains("CROSSOVER_START")) {
                    this.crossoverStart = Integer.parseInt(secondString(row));
                }

                if (row.contains("CROSSOVER_END")) {
                    this.crossoverEnd = Integer.parseInt(secondString(row));
                }
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Problem parsing the GA parameters config file.");
            System.exit(1);
        }

        // Data check
        boolean allGoodData = true;
        if (this.generations <= 0)        allGoodData = false;
        if (this.population < 2)          allGoodData = false;
        if (this.genes < 1)               allGoodData = false;
        if (this.mutationFrequency1 < 0 ) allGoodData = false;
        if (this.mutationFrequency2 < 0)  allGoodData = false;
        if (this.parentFate.isEmpty())    allGoodData = false;
        if (this.mutationStart < 0)       allGoodData = false;
        if (this.mutationEnd < 0)         allGoodData = false;
        if (this.crossoverFrequency < 0)  allGoodData = false;
        if (this.crossoverStart < 0)      allGoodData = false;
        if (this.crossoverEnd < 0)        allGoodData = false;

        if (!allGoodData) {
            Assertions.log("FATAL ERROR : Good preferences not returned from GA configuration file.");
            System.exit(1);
        }
    }

    // Gets
    public int    getGenerations()           { return this.generations;        }
    public int    getPopulation()            { return this.population;         }
    public int    getGenes()                 { return this.genes;              }
    public double getMutationFrequency1()    { return this.mutationFrequency1; }
    public double getMutationFrequency2()    { return this.mutationFrequency2; }
    public String getMutationMethod()        { return this.mutationMethod;     }
    public String getParentFate()            { return this.parentFate;         }
    public int    getMutationStart()         { return this.mutationStart;      }
    public int    getMutationEnd()           { return this.mutationEnd;        }
    public double getCrossoverFrequency()    { return this.crossoverFrequency; }
    public int    getCrossoverStart()        { return this.crossoverStart;     }
    public int    getCrossoverEnd()          { return this.crossoverEnd;       }

    // Sets

    // Methods

    private String secondString(String s) {
        String[] parts = s.split(" +");
        return parts[1];
    }
}
































