
package nbn.common.taxon;

public class TaxonAbsence extends TaxonObservation {
    public TaxonAbsence(int key) {
        super(key);
    }

    @Override
    public Abundance getAbundance() {
        return Abundance.ABSENCE;
    }
}
