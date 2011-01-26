package gen_seq.generator;

/**
 *
 * @author akademi4eg
 */
public class GenesPair {
    public GenesPair(Gene oldG, Gene newG) {
        this.oldG = oldG;
        this.newG = newG;
    }

    public Gene getOld() {
        return oldG;
    }

    public Gene getNew() {
        return newG;
    }

    private Gene oldG;
    private Gene newG;
}
