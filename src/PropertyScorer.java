import java.util.ArrayList;

public class PropertyScorer implements Scorer {
    private ArrayList<Datum> population;

    public PropertyScorer(ArrayList<Datum> population) {
        this.population = population;
    }

    @Override
    public double score(Datum datum) {
        return datum.score;
    }

    // New method
    public double score(int a, int b, int c, int d, int e) {
        for (Datum dd : population) {
            if (dd.a == a && dd.b == b && dd.c == c && dd.d == d && dd.e == e) {
                return dd.score;
            }
        }
        throw new IllegalArgumentException("No Datum found for: " + a + "," + b + "," + c + "," + d + "," + e);
    }
}