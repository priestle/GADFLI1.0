import java.util.ArrayList;

public class PropertyScorer implements Scorer {

    @Override
    public double score(Datum datum) {

        System.out.println("This is the fucker we're looking at..." + datum);

        return datum.score;

    }

}