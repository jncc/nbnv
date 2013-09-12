/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.List;

/**
 *
 * @author Matt Debont
 */
public class DatasetAccessPositionsJSON {
    private String publicAccess;
    private List<String> enhanced;

    public String getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(String publicAccess) {
        this.publicAccess = publicAccess;
    }

    public List<String> getEnhanced() {
        return enhanced;
    }

    public void setEnhanced(List<String> enhanced) {
        this.enhanced = enhanced;
    }
}
