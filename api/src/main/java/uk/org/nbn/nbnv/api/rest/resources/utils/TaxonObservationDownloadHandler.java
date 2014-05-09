/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.model.Attribute;
import uk.org.nbn.nbnv.api.model.TaxonObservationDownload;

/**
 *
 * @author Matt Debont
 */
public class TaxonObservationDownloadHandler implements ResultHandler {

    final private ZipOutputStream zip;
    final private boolean includeAttributes;
    final private List<Attribute> attributes;
    private Map<String, Integer> datasetRecordCounts;
    private SimpleDateFormat df;
    private DownloadHelper downloadHelper;

    public TaxonObservationDownloadHandler(ZipOutputStream zip, boolean includeAttributes, List<Attribute> attributes, DownloadHelper downloadHelper) {
        this.zip = zip;
        this.includeAttributes = includeAttributes;
        this.attributes = attributes;
        this.datasetRecordCounts = new HashMap<String, Integer>();
        this.df = new SimpleDateFormat("dd/MM/yyyy");
        this.downloadHelper = downloadHelper;
    }

    public Map<String, Integer> returnDatasetRecordCounts() {
        return datasetRecordCounts;
    }

    @Override
    public void handleResult(ResultContext context) {
        TaxonObservationDownload observation = (TaxonObservationDownload) context.getResultObject();
        
        List<String> values = new ArrayList<String>();
        values.add(Integer.toString(observation.getObservationID()));
        values.add(observation.getObservationKey());
        values.add(observation.getOrganisationName());
        values.add(observation.getDatasetKey());
        values.add(StringUtils.hasText(observation.getSurveyKey()) ? observation.getSurveyKey() : "");
        values.add(StringUtils.hasText(observation.getSampleKey()) ? observation.getSampleKey() : "");
        values.add(StringUtils.hasText(observation.getGridReference()) ? observation.getGridReference() : "");
        values.add(StringUtils.hasText(observation.getPrecision()) ? observation.getPrecision() : "");
        values.add(StringUtils.hasText(observation.getSiteKey()) ? observation.getSiteKey() : "");
        values.add(StringUtils.hasText(observation.getSiteName()) ? observation.getSiteName() : "");
        values.add(StringUtils.hasText(observation.getFeatureKey()) ? observation.getFeatureKey() : "");
        values.add(observation.getStartDate() != null ? df.format(observation.getStartDate()) : "");
        values.add(observation.getEndDate() != null ? df.format(observation.getEndDate()) : "");
        values.add(observation.getDateType());
        values.add(StringUtils.hasText(observation.getRecorder()) ? observation.getRecorder() : "");
        values.add(StringUtils.hasText(observation.getDeterminer()) ? observation.getDeterminer() : "");
        values.add(observation.getpTaxonVersionKey());
        values.add(observation.getpTaxonName());
        values.add(observation.getAuthority());
        values.add(StringUtils.hasText(observation.getCommonName()) ? observation.getCommonName() : "");
        values.add(observation.getTaxonGroup());
        values.add(observation.isSensitive() ? "true" : "false");
        values.add(observation.isZeroAbundance() ? "true" : "false");
        values.add(observation.isFullVersion() ? "true" : "false");

        if (includeAttributes) {
            if (observation.isFullVersion() || observation.isPublicAttribute()) {
                Map<String, String> obsAttribs = new HashMap<String, String>();

                String[] attVals = org.apache.commons.lang.StringUtils.split(observation.getAttrStr(), "¦");
                for (String attVal : attVals) {
                    String[] vals = org.apache.commons.lang.StringUtils.split(attVal, "¬");
                    obsAttribs.put(vals[0], vals[1]);
                }
                
                for (Attribute att : attributes) {
                    if (obsAttribs.containsKey(att.getLabel())) {
                        values.add(obsAttribs.get(att.getLabel()));
                    } else {
                        values.add("");
                    }
                }
            } else {
                for (int i = 0; i < attributes.size(); i++) {
                    values.add("");
                }
            }
        }

        if (datasetRecordCounts.containsKey(observation.getDatasetKey())) {
            datasetRecordCounts.put(observation.getDatasetKey(), datasetRecordCounts.get(observation.getDatasetKey()) + 1);
        } else {
            datasetRecordCounts.put(observation.getDatasetKey(), 1);
        }        
        
        try {
            downloadHelper.writelnCsv(zip, values);
        } catch (IOException ex) {
            // TODO: Do something
        }
    }
}
