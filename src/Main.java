import java.util.ArrayList;

public class Main {

    /*
     * GADFLI: Genetic Algorithm Discrimination For Ligand Identification
     *
     * (c) 2025 Nigel D. Priestley, Ph.D. / Promiliad Biopharma Incorporated
     * All rights reserved.
     *
     * Description:
     *   Entry point for the GADFLI NPFR-molecule search platform.
     *   Supports modular search strategies including BeamACO, Tabu Search,
     *   and GRASP. Designed for high-performance ligand evaluation using
     *   GNINA, MMPBSA, and other surrogate scoring functions.
     *
     * Usage:
     *   java -jar gadfli.jar --method [beam|tabu|grasp] --config path/to/config.ini
     *
     * Configuration:
     *   Modular INI-style configs support included files, section overrides,
     *   checkpointing, and SLURM-Aptainer HPC workflows.
     *
     * Notes:
     *   This software is distributed for academic and internal research use only.
     *   Redistribution or commercial use requires written permission.
     *
     * Containerization:
     *   Compatible with Apptainer/Singularity. Java runtime and GNINA can be
     *   bundled into the container image for deployment to SLURM-based HPC clusters.
     *
     * Author:
     *   Nigel D. Priestley, Ph.D. <nigel.d.priestley@promiliad.com>
     *
     * Revision:
     *   July 2025
     */

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Usage: gadfli <command> [options]");
            System.err.println("Available commands:");
            System.err.println("  run       Launch full GADFLI optimization");
            System.err.println("  help      Show this help message");
            System.exit(1);
        }

        switch (args[0].toLowerCase()) {
            case "run":
                runGadfli(args);
                break;

            case "help":
            case "--help":
            case "-h":
                printHelp();
                break;

            default:
                System.err.println("Unknown command: " + args[0]);
                System.err.println("Use 'help' to see available commands.");
                System.exit(1);
        }
    }

    private static void runGadfli (String[] args) {

        Config.init(args[1]);
        Assertions.init();
        Helpers.init();
        // TODO: Write a data init? ... what would a data class look like?
        //     : 4 vs 5 mers, data type, restricted monomers...
        ArrayList<Datum> population = new ArrayList<>();

        Scorer scorer = new VinaScorer();
        Engine engine = new GAEngine(scorer);

    }

    private static void printHelp() {
        System.out.println("GADFLI - Genetic Algorithm Discrimination for Ligand Identification");
        System.out.println("Usage: gadfli <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  run         Launch GADFLI optimization");
        System.out.println("  help        Show this help message");
        // TODO: List more commands like validate, score, debug, etc.

    }
}








































