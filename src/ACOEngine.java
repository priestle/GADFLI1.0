import java.util.ArrayList;

public class ACOEngine extends Engine {
    private final Scorer scorer;

    public ACOEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running GA Engine...");
        for (Datum cand : population) {
            double score = scorer.score(cand);
            System.out.printf("  %s scored %.3f\n", cand, score);
        }
    }
}