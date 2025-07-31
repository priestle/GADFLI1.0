import java.util.ArrayList;

public class RandomEngine extends Engine {
    private final Scorer scorer;

    public RandomEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running Random Engine...");
        for (Datum cand : population) {
            double score = scorer.score(cand);
            System.out.printf("  %s scored %.3f\n", cand, score);
        }
    }
}