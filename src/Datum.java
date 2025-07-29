public class Datum {

    // Variables
    public int a, b, c, d, e;
    public double score;

    // Constructor
    public Datum( int a, int b, int c, int d, int e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d,%d,%d] : %f", this.a, this.b, this.c, this.d, this.e, this.score);
    }
}
