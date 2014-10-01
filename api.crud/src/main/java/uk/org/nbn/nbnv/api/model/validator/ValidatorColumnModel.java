/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.validator;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
public class ValidatorColumnModel {
    private String jobName;
    private List<ColumnMapping> mappings;
    private List<DarwinCoreField> fields;
    
    public ValidatorColumnModel() {
        
    }
    
    public ValidatorColumnModel(String jobName, List<ColumnMapping> mappings, List<DarwinCoreField> fields) {
        this.jobName = jobName;
        this.mappings = mappings;
        this.fields = fields;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<ColumnMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<ColumnMapping> mappings) {
        this.mappings = mappings;
    }

    public List<DarwinCoreField> getFields() {
        return fields;
    }

    public void setFields(List<DarwinCoreField> fields) {
        this.fields = fields;
    }        
}
