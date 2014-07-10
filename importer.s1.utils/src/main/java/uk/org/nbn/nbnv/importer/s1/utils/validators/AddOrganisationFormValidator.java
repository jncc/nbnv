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
import uk.org.nbn.nbnv.importer.s1.utils.model.AddOrganisationForm;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.jpa.nbncore.User;

/**
 *
 * @author Matt Debont
 */
public class AddOrganisationFormValidator implements Validator {

    private final Validator organisationValidator;
    
    public AddOrganisationFormValidator(Validator organisationValidator) {
        if (organisationValidator == null) {
            throw new IllegalArgumentException("The supplied validator is required and must not be null");
        }
        if (!organisationValidator.supports(Organisation.class)) {
            throw new IllegalArgumentException("The supplied validator must support the validation of Organisation instances");
        }
        this.organisationValidator = organisationValidator;
    }
    
    @Override
    public boolean supports(Class<?> type) {
        return AddOrganisationForm.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddOrganisationForm form = (AddOrganisationForm) o;
        
        try {
            errors.pushNestedPath("organisation");
            ValidationUtils.invokeValidator(this.organisationValidator, form.getOrganisation(), errors);
            
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("User.findByEmail");
            q.setParameter("email", form.getOrganisationAdmin());

            List res = q.getResultList();
            em.close();
            if (res.size() == 1) {
                User user = (User) res.get(0);
                form.setAdminID(user.getId());
            } else {
                errors.rejectValue("organisationAdmin", "organisationAdmin.notFound");
            }
            
        } finally {
            errors.popNestedPath();
        }
    }
    
}
