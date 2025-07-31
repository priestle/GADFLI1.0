import java.util.ArrayList;

public class PSOEngine extends Engine {
    private final Scorer scorer;

    public PSOEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running PSO Engine...");
        for (Datum cand : population) {
            double score = scorer.score(cand);
            System.out.printf("  %s scored %.3f\n", cand, score);
        }
    }
}