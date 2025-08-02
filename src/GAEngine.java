import java.util.ArrayList;
import java.util.Random;

public class GAEngine extends Engine {
    private final Scorer scorer;

    public GAEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running GA Engine...");

        GAEngineParameters engineParameters = new GAEngineParameters(Config.calculationRoot + "/" + Config.engineConfigFileName);

        MonomerDefs monomerDefs = new MonomerDefs(Config.calculationRoot + "/" + Config.monomerDefinitionsFilename);

        if (Config.modelType.equals("NPFR")) {
            // todo: read in the sequence definition file to translate to NPFR numbers...
            // todo: this isn't needed by the other model types...
        }

        Assertions.log("Generating initial population...");

        // I have an element 1,2,3,4,0 and I want to pull it's score from the population,
        // but I don't know which population member that is?

    }
}