public class GninaScorer implements Scorer {

    @Override
    public double score(Datum datum) {

        return -8.0 * Math.random() * 2.0;
    }

}