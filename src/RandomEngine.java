import java.util.ArrayList;
import java.util.Random;

public class RandomEngine extends Engine {
    private final Scorer scorer;

    public RandomEngine(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void run(ArrayList<Datum> population) {
        System.out.println("Running Random Engine...");

        /**
         *      The question to be answered here is how effective is our assumed worst case,
         *      randomly picking members of the set to evaluate. The benchmarks are:
         *
         *      1) Number of evaluations to get the first 10 members in the top 0.1%.
         *
         *      2) If we run 25,000 evaluations, how many are in the top 0.1%
         */

        Assertions.log("Starting random.engine...");

        ArrayList<Double> enrichments = new ArrayList<>();
        ArrayList<Integer> firsts      = new ArrayList<>();
        System.out.println("\n");

        int maxExperiments = 1000;
        int numberToScreen = 49794;

        for (int j = 0; j < maxExperiments; j++) {
            int eliteCount = 0;
            Random random = new Random();
            int firstElite = 0;

            for (int i = 0; i < numberToScreen; i++) {

                int candidateNumber = random.nextInt(population.size());

                Datum dat = population.get(candidateNumber);

                if (dat.rank < 0.001) {
                    eliteCount++;
                    if (eliteCount == 1) {
                        firstElite = i;
                    }
                }
            }
            double percentComplete = (double)j/maxExperiments*100;
            if (j % 50 == 0) System.out.printf("\r%.1f%%", percentComplete);
            double enrichment = (double)eliteCount / numberToScreen;
            enrichments.add(enrichment);
            firsts.add(firstElite);
        }
        System.out.println("\n");

        // Statistics
        double enrichmentsAverage = 0.0;
        double firstsAverage      = 0.0;
        for (int i = 0; i < enrichments.size(); i++) {
            enrichmentsAverage += enrichments.get(i);
            firstsAverage += firsts.get(i);
        }
        enrichmentsAverage = enrichmentsAverage / enrichments.size();
        firstsAverage = firstsAverage / firsts.size();

        double enrichmentsDiff = 0.0;
        double firstsDiff = 0.0;

        for (int i = 0; i < enrichments.size(); i++) {
            enrichmentsDiff += Math.pow(enrichments.get(i)-enrichmentsAverage,2);
            firstsDiff += Math.pow(firsts.get(i)-firstsAverage,2);
        }

        double enrichmentsSD = Math.pow(enrichmentsDiff / (enrichments.size()-1), 0.5);
        double firstsSD = Math.pow(firstsDiff / (firsts.size()-1), 0.5);

        Assertions.log("First elite found at " + firstsAverage + " +/- " + firstsSD);
        Assertions.log("Enrichment : " + enrichmentsAverage + " +/- " + enrichmentsSD);

        /*
        for (Datum cand : population) {
            double score = scorer.score(cand);
            System.out.printf("  %s scored %.3f\n", cand, score);
        }

         */
    }
}