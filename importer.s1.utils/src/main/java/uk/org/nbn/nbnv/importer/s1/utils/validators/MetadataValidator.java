/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.validators;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.jpa.nbncore.User;

/**
 *
 * @author Matt Debont
 */
public class MetadataValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Metadata.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "methods", "methods.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "purpose", "purpose.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quality", "quality.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "access", "access.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "geographicalRes", "geographicalRes.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recordAtts", "recordAtts.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recorderNames", "recorderNames.required");
        
        Metadata meta = (Metadata) o;
        
        // Replace nulls with false for Recorder Names / Record Atts options
        if (meta.getRecordAtts().equals("null")) {
            meta.setRecordAtts("false");
        }
        if (meta.getRecorderNames().equals("null")) {
            meta.setRecorderNames("false");
        }
        
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("User.findByEmail");
        q.setParameter("email", meta.getDatasetAdminEmail());
        
        List res = q.getResultList();
        em.close();
        if (res.size() == 1) {
            User user = (User) res.get(0);
            meta.setDatasetAdminID(user.getId());
        } else {
            errors.rejectValue("datasetAdminEmail", "datasetAdminEmail.notFound");
        }
        
        if (meta.getGeographicalRes().equals("Full")) {
            if (meta.getRecordAtts().trim().isEmpty() || meta.getRecordAtts() == null) {
                errors.rejectValue("recordAtts", "record.geoResFull");
            }
            if (meta.getRecorderNames().trim().isEmpty() || meta.getRecorderNames() == null) {
                errors.rejectValue("recorderNames", "record.geoResFull");
            }
        } else {
            if (meta.getRecordAtts().equals("Yes")) {
                errors.rejectValue("recordAtts", "record.geoResNotFull");
            }
            if (meta.getRecorderNames().equals("Yes")) {
                errors.rejectValue("recorderNames", "record.geoResNotFull");
            }            
        }
    }
}
