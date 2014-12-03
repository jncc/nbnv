/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.tools;

import java.text.Normalizer;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.model.MetadataForm;

/**
 *
 * @author Matt Debont
 */
public class TextTools {
    public static Metadata cleanMetadataTextInputs(Metadata metadata) {       
        metadata.setAccess(normalizeAndTrim(metadata.getAccess()));
        metadata.setDatasetAdminEmail(normalizeAndTrim(metadata.getDatasetAdminEmail()));
        metadata.setDatasetAdminName(normalizeAndTrim(metadata.getDatasetAdminName()));
        metadata.setDatasetAdminPhone(normalizeAndTrim(metadata.getDatasetAdminPhone()));
        metadata.setDescription(normalizeAndTrim(metadata.getDescription()));
        metadata.setGeographic(normalizeAndTrim(metadata.getGeographic()));
        metadata.setInfo(normalizeAndTrim(metadata.getInfo().trim()));
        metadata.setMethods(normalizeAndTrim(metadata.getMethods()));
        metadata.setPurpose(normalizeAndTrim(metadata.getPurpose()));
        metadata.setQuality(normalizeAndTrim(metadata.getQuality()));
        metadata.setTemporal(normalizeAndTrim(metadata.getTemporal()));
        metadata.setUse(normalizeAndTrim(metadata.getUse()));
        //metadata.setUse(Normalizer.normalize(metadata.getUse(), Normalizer.Form.NFD).replaceAll("âââââ", "").replaceAll("     ", "").trim()); 
       
        return metadata;
    }
    
    public static String normalizeAndTrim(String input) {
        if (input != null) {
            return Normalizer.normalize(input.trim(), Normalizer.Form.NFKD).replaceAll("âââââ", "").replaceAll("     ", "");
        }
        return input;
    }    
}
