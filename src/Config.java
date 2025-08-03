import java.nio.file.Paths;
import java.util.Scanner;

public final class Config {

    public static String configFileName              = "";
    public static String gadfliVersion               = "";
    public static String modelName                   = "";
    public static String calculationRoot             = "";
    public static String debugStatus                 = "";
    public static String loggingStatus               = "";
    public static String overwriteStatus             = "";
    public static String archiveFileName             = "";
    public static String monomerDefinitionsFilename  = "";
    public static String sequenceDefinitionsFileName = "";
    public static String logFileName                 = "";
    public static String summaryFileName             = "";
    public static String historyFileName             = "";
    public static String restartFileName             = "";
    public static String molFileDirectoryRootName    = "";
    public static String engineType                  = "";
    public static String engineConfigFileName        = "";
    public static String modelType                   = "";
    public static String scoringConfigFileName       = "";
    public static String helperConfigFileName        = "";
    public static String OSType                      = "";

    public static void init(String cfg) {
        String configFileName = cfg;
        try (Scanner inputF = new Scanner(Paths.get(cfg))) {
            while (inputF.hasNext()) {
                String row = inputF.nextLine();

                if (row.contains("GADFLI_VERSION")) {
                    gadfliVersion = getSecondPart(row);
                }

                if (row.contains("MODEL_NAME")) {
                    modelName = getSecondPart(row);
                }

                if (row.contains("CALCULATION_ROOT")) {
                    calculationRoot = getSecondPart(row);
                }

                if (row.contains("DEBUG")) {
                    debugStatus = getSecondPart(row);
                }

                if (row.contains("LOGGING")) {
                    loggingStatus = getSecondPart(row);
                }

                if (row.contains("OVERWRITE")) {
                    overwriteStatus = getSecondPart(row);
                }

                if (row.contains("ARCHIVE_FILE")) {
                    archiveFileName = getSecondPart(row);
                }

                if (row.contains("MONOMER_DEFINITIONS")) {
                    monomerDefinitionsFilename = getSecondPart(row);
                }

                if (row.contains("SEQUENCE_DEFINITIONS")) {
                    sequenceDefinitionsFileName = getSecondPart(row);
                }

                if (row.contains("LOGFILE")) {
                    logFileName = getSecondPart(row);
                }

                if (row.contains("SUMMARY_FILE")) {
                    summaryFileName = getSecondPart(row);
                }

                if (row.contains("HISTORY_FILE")) {
                    historyFileName = getSecondPart(row);
                }

                if (row.contains("RESTART_FILE")) {
                    restartFileName = getSecondPart(row);
                }

                if (row.contains("MOLFILE_ROOT")) {
                    molFileDirectoryRootName = getSecondPart(row);
                }

                if (row.contains("ENGINE_TYPE")) {
                    engineType = getSecondPart(row);
                }

                if (row.contains("ENGINE_CONFIG")) {
                    engineConfigFileName = getSecondPart(row);
                }

                if (row.contains("MODEL_TYPE")) {
                    modelType = getSecondPart(row);
                }

                if (row.contains("SCORING_CONFIG")) {
                    scoringConfigFileName = getSecondPart(row);
                }

                if (row.contains("HELPER_CONFIG")) {
                    helperConfigFileName = getSecondPart(row);
                }

                if (row.contains("OS_TYPE")) {
                    OSType = getSecondPart(row);
                }
            }


            boolean settingsAreComplete = true;

            if (configFileName.isEmpty())                   { settingsAreComplete = false; }
            if (gadfliVersion.isEmpty())                    { settingsAreComplete = false; }
            if (modelName.isEmpty())                        { settingsAreComplete = false; }
            if (calculationRoot.isEmpty())                  { settingsAreComplete = false; }
            if (debugStatus.isEmpty())                      { settingsAreComplete = false; }
            if (loggingStatus.isEmpty())                    { settingsAreComplete = false; }
            if (overwriteStatus.isEmpty())                  { settingsAreComplete = false; }
            if (archiveFileName.isEmpty())                  { settingsAreComplete = false; }
            if (monomerDefinitionsFilename.isEmpty())       { settingsAreComplete = false; }
            if (sequenceDefinitionsFileName.isEmpty())      { settingsAreComplete = false; }
            if (logFileName.isEmpty())                      { settingsAreComplete = false; }
            if (summaryFileName.isEmpty())                  { settingsAreComplete = false; }
            if (historyFileName.isEmpty())                  { settingsAreComplete = false; }
            if (restartFileName.isEmpty())                  { settingsAreComplete = false; }
            if (molFileDirectoryRootName.isEmpty())         { settingsAreComplete = false; }
            if (engineType.isEmpty())                       { settingsAreComplete = false; }
            if (engineConfigFileName.isEmpty())             { settingsAreComplete = false; }
            if (modelType.isEmpty())                        { settingsAreComplete = false; }
            if (scoringConfigFileName.isEmpty())            { settingsAreComplete = false; }
            if (helperConfigFileName.isEmpty())             { settingsAreComplete = false; }
            if (OSType.isEmpty())                           { settingsAreComplete = false; }

            if (!settingsAreComplete) {
                String aS = "CONFIG SETTINGS : \n";
                aS += "\tGeneral:\n";
                aS += "\t" + "Config file name                : " + configFileName              + "\n";
                aS += "\t" + "GADFLI version                  : " + gadfliVersion               + "\n";
                aS += "\t" + "Model name                      : " + modelName                   + "\n";
                aS += "\t" + "Calculation root                : " + calculationRoot             + "\n";
                aS += "\t" + "Debug status                    : " + debugStatus                 + "\n";
                aS += "\t" + "Logging status                  : " + loggingStatus               + "\n";
                aS += "\t" + "Overwrite status                : " + overwriteStatus             + "\n";
                aS += "\t" + "Archive file name               : " + archiveFileName             + "\n";
                aS += "\t" + "Monomers to be used             : " + monomerDefinitionsFilename  + "\n";
                aS += "\t" + "Sequence definitions            : " + sequenceDefinitionsFileName + "\n";
                aS += "\t" + "Log file                        : " + logFileName                 + "\n";
                aS += "\t" + "Summary file                    : " + summaryFileName             + "\n";
                aS += "\t" + "History file                    : " + historyFileName             + "\n";
                aS += "\t" + "Restart file                    : " + restartFileName             + "\n";
                aS += "\t" + "Mol file directory root         : " + molFileDirectoryRootName    + "\n";
                aS += "\t" + "Engine type                     : " + engineType                  + "\n";
                aS += "\t" + "Engine config file name         : " + engineConfigFileName        + "\n";
                aS += "\t" + "Model type                      : " + modelType                   + "\n";
                aS += "\t" + "Scoring config file name        : " + scoringConfigFileName       + "\n";
                aS += "\t" + "Helper program config file name : " + helperConfigFileName        + "\n";
                aS += "\t" + "OS TYpe                         : " + OSType                      + "\n";
                System.err.println("The main config file is not complete. There is no point to going further until fixed.");
                System.err.println(aS);
            }
        }
        catch (Exception e) {
            System.err.println("Error: config file not found or unreadable.");
            e.printStackTrace();
        }


    }

    // Gets
    public String getConfigFileName() { return configFileName; }

    // Methods
    private static String getSecondPart(String s) {
        String[] parts = s.split(" +");
        return parts[1];
    }
}
