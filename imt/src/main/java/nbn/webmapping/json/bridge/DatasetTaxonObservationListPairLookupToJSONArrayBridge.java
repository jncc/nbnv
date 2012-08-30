
package nbn.webmapping.json.bridge;

import nbn.common.bridging.AbstractNameLookupableBridge;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.TaxonDataset;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.TaxonObservation;
import org.json.JSONArray;

public class DatasetTaxonObservationListPairLookupToJSONArrayBridge extends AbstractNameLookupableBridge<DatasetTaxonObservationListPair<?>, JSONArray> {
    private Bridge<TaxonDataset,String> datasetLookupBridge = new TaxonDatasetToJSONObjectBridge().getNamedLookupBridge();
    private Bridge<TaxonObservation,String> taxonObservationLookupBridge = new TaxonObservationToJSONObjectBridge().getNamedLookupBridge();
    
    public JSONArray convert(DatasetTaxonObservationListPair<?> toConvert) throws BridgingException {
        return new JSONArray(new ListBridge<TaxonObservation, String>(taxonObservationLookupBridge).convert(toConvert.getTaxonObservationList()));
    }

    public String getLookupableName(DatasetTaxonObservationListPair<?> toConvert) throws BridgingException {
        return datasetLookupBridge.convert(toConvert.getDataset());
    }
}

