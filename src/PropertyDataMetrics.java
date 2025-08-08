import java.util.ArrayList;

public final class PropertyDataMetrics {

    // Public, constant after init()
    public static int    N;
    public static double MEAN;
    public static double STDEV;
    public static double MIN;
    public static double MAX;
    public static double THRESHOLD1_0;
    public static double THRESHOLD0_3;
    public static double THRESHOLD0_1;

    private static boolean initialized = false;

    private PropertyDataMetrics() {} // prevent instantiation

    public static void init(ArrayList<Datum> population) {
        if (initialized) {
            throw new IllegalStateException("PropertyDataMetrics already initialized.");
        }
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population is null or empty.");
        }

        N = population.size();

        MIN = Double.POSITIVE_INFINITY;
        MAX = Double.NEGATIVE_INFINITY;
        double sum = 0.0;

        for (Datum d : population) {
            double v = d.score; // or d.getScore()
            if (v < MIN) MIN = v;
            if (v > MAX) MAX = v;
            sum += v;
        }

        MEAN = sum / N;

        double varSum = 0.0;
        for (Datum d : population) {
            double v = d.score;
            double dv = v - MEAN;
            varSum += dv * dv;
        }
        STDEV = (N > 1) ? Math.sqrt(varSum / (N - 1)) : 0.0;

        int PC1_0 = (int)(population.size() * 0.01);
        int PC0_3 = (int)(population.size() * 0.003);
        int PC0_1 = (int)(population.size() * 0.001);

        THRESHOLD1_0 = population.get(PC1_0).score;
        THRESHOLD0_3 = population.get(PC0_3).score;
        THRESHOLD0_1 = population.get(PC0_1).score;


        initialized = true;
    }
}
