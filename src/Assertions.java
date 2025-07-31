import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Assertions {

    private Assertions() {}

    public static void init() {

        File logfile = new File(Config.logFileName);

        if ("YES".equals(Config.overwriteStatus)) {
            if (logfile.exists()) {
                if (!logfile.delete()) {
                    System.out.println("FATAL PROBLEM: Could not delete existing logfile.\n");
                    System.exit(1);
                }
            }
            try {
                if (logfile.createNewFile()) {
                    System.out.println("Log file generated. \n");
                } else {
                    System.out.println("FATAL PROBLEM : Log file exists or could not be created. \n");
                    System.exit(1);
                }
            }
            catch (IOException e) {
                System.out.println("FATAL PROBLEM : Can't create new log file. \n");
                e.printStackTrace();
                System.exit(1);
            }
        }

        if ("NO".equals(Config.overwriteStatus)) {
            if (!logfile.exists()) {
                System.out.println("WARNING : Trying to append to a logfile that doesn't exist. \n");
                try {
                    if (logfile.createNewFile()) {
                        System.out.println("New log file generated. \n");
                    } else {
                        System.out.println("FATAL PROBLEM : Can't create new log file. \n");
                        System.exit(1);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

    }

    public static void log(String logString) {

        // check logging status
        boolean goodLoggingStatus = false;
        if (Config.loggingStatus.equals("ALL"))      { goodLoggingStatus = true; }
        if (Config.loggingStatus.equals("FILEONLY")) { goodLoggingStatus = true; }
        if (Config.loggingStatus.equals("TEXTONLY")) { goodLoggingStatus = true; }
        if (Config.loggingStatus.equals("NONE"))     { goodLoggingStatus = true; }
        if (!goodLoggingStatus) {
            System.out.println("PROBLEM: Invalid logging status choice in config file.");
            System.exit(1);
        }
        if (Config.loggingStatus.equals("TEXTONLY")) {
            System.out.println(logString);
        }
        if (Config.loggingStatus.equals("FILEONLY")) {
            try (FileWriter outputF = new FileWriter(Config.logFileName, true)) {
                outputF.write(logString + "\n");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Config.loggingStatus.equals("ALL")) {
            System.out.println(logString);
            try (FileWriter outputF = new FileWriter(Config.logFileName, true)) {
                outputF.write(logString + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void AssertRunDateTime() {
        LocalDateTime currentDateTime;
        currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        log("STARTING RUN AT :" + formattedDateTime + "\n");
    }
}
