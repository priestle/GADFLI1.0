import java.nio.file.Paths;
import java.util.Scanner;

public final class Helpers {

    public static String obabelProgram     = "";
    public static String obpropertyProgram = "";
    public static String vinaProgram       = "";
    public static String vinaSplitProgram  = "";
    public static String gninaProgram      = "";

    public static void init() {
        try (Scanner inputF = new Scanner(Paths.get(Config.calculationRoot + "/" + Config.helperConfigFileName))) {
            while (inputF.hasNextLine()) {
                String row = inputF.nextLine();

                if (row.contains("OBABEL"))         { obabelProgram = getSecondPart(row); }
                if (row.contains("OBPROPERTY"))     { obpropertyProgram = getSecondPart(row); }
                if (row.contains("VINA_CMD"))       { vinaProgram = getSecondPart(row); }
                if (row.contains("VINA_SPLIT_CMD")) { vinaSplitProgram = getSecondPart(row); }
                if (row.contains("GNINA_CMD"))      { gninaProgram = getSecondPart(row); }
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL PROBLEM : Helper config missing or unreadable.");
            e.printStackTrace();
        }

        boolean settingsAreComplete = true;

        if (obabelProgram.isEmpty())     settingsAreComplete = false;
        if (obpropertyProgram.isEmpty()) settingsAreComplete = false;
        if (vinaProgram.isEmpty())       settingsAreComplete = false;
        if (vinaSplitProgram.isEmpty())  settingsAreComplete = false;
        if (gninaProgram.isEmpty())      settingsAreComplete = false;

        if (!settingsAreComplete) {
            Assertions.log("FATAL PROBLEM : Errors in helper program definitions.");
        }

    }

    private static String getSecondPart(String s) {
        String[] parts = s.split(" +");
        return parts[1];
    }
}
