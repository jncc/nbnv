/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import uk.org.nbn.nbnv.api.model.AccessPosition;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class DatasetAccessPositionsJSON {
    private String publicAccess;
    private List<AccessPosition> enhanced;

    public String getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(String publicAccess) {
        this.publicAccess = publicAccess;
    }

    public List<AccessPosition> getEnhanced() {
        return enhanced;
    }

    public void setEnhanced(List<AccessPosition> enhanced) {
        this.enhanced = enhanced;
    }
}
