import java.util.ArrayList;

public interface Scorer {
    double score(Datum datum);
    double score(int a, int b, int c, int d, int e);
}
