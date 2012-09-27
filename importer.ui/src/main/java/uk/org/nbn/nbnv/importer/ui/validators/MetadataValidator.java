/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.validators;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.UserData;

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
        
        Metadata meta = (Metadata) o;
        
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("UserData.findByEmail");
        q.setParameter("email", meta.getDatasetAdminEmail());
        
        List res = q.getResultList();
        if (res.size() == 1) {
            UserData user = (UserData) res.get(0);
            meta.setDatasetAdminID(user.getId());
        } else {
            errors.rejectValue("datasetAdminEmail", "datasetAdminEmail.notFound");
        }
        
        if (meta.getGeographicalRes().equals("full")) {
            if (meta.getRecordAtts().trim().isEmpty() || meta.getRecordAtts() == null) {
                errors.rejectValue("recordAtts", "record.geoResFull");
            }
            if (meta.getRecorderNames().trim().isEmpty() || meta.getRecorderNames() == null) {
                errors.rejectValue("recorderNames", "record.geoResFull");
            }
        } else {
            if (meta.getRecordAtts().equals("yes")) {
                errors.rejectValue("recordAtts", "");
            }
            if (meta.getRecorderNames().equals("yes")) {
                errors.rejectValue("recorderNames", "record.getResNotFull");
            }            
        }
    }
}
