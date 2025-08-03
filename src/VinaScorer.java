public class VinaScorer implements Scorer {

    @Override
    public double score(Datum datum) {

        return -8.0 * Math.random() * 2.0;
    }

    @Override
    public double score(int a, int b, int c, int d, int e) {
        return 0;
    }

}
