package nbn.common.taxon;

public class TaxonPresence extends TaxonObservation {
    public TaxonPresence(int key) {
        super(key);
    }

    @Override
    public Abundance getAbundance() {
        return Abundance.PRESENCE;
    }
}
