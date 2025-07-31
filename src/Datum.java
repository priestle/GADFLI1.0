public class Datum {

    // Variables
    public int a, b, c, d, e;
    public double score = 0.0;
    public double rank  = 0.00;
    public String type  = "-----";

    // Constructor
    public Datum( int a, int b, int c, int d, int e, double score, double rank, String type) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.score = score;
        this.rank = rank;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d,%d,%d] : %f %f %s", this.a, this.b, this.c, this.d, this.e,
                this.score, this.rank, this.type
        );
    }
}
