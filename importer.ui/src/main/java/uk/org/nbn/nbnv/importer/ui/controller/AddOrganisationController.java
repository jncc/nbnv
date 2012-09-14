/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uk.org.nbn.nbnv.importer.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
@Controller
public class AddOrganisationController {
    @Autowired SessionData sessionData;
    
    private String errorMsg = "";
    
    @RequestMapping(value="/addOrganisation.html", method=RequestMethod.GET)
    public ModelAndView addOrganisation() {
       AddOrganisationForm model = new AddOrganisationForm();
       return new ModelAndView("addOrganisation", "model", model);
    }   
    
    @RequestMapping(value="/addOrganisationProcess.html", method=RequestMethod.POST)
    public ModelAndView processNewOrganisation(AddOrganisationForm model, BindingResult result) {
        
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }
            return new ModelAndView("addOrganisation", "model", model);
        }
        
        // TODO Put moar Validators in!!!!
        runValidators(model);
        
        if (model.getErrors().isEmpty()) {           
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            em.getTransaction().begin();
            em.persist(model.getOrganisation());
            em.getTransaction().commit();
        } else {
            Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{"0", "One or more of the Validators failed while adding organisation"});
            return new ModelAndView("addOrganisation", "model", model);
        }
        
        // Return to metadata input form, should probably find a way of auto-selecting the new organisation
        return new ModelAndView("redirect:/metadata.html", "model", new MetadataForm());
    }
    
    private void runValidators(AddOrganisationForm model) {
//        try {
//            Class cls = Class.forName("AddOrganisationController");
//            Method methodList[] = cls.getDeclaredMethods();
//            
//            Pattern pattern = Pattern.compile("(a-z)*Validator$");
//            
//            for (Method method : methodList) {
//                if (pattern.matcher(method.getName()).matches()) {
//                    try {
//                        Object retObj = method.invoke(this, organisation);
//                        boolean retVal = (Boolean)retObj;
//                        if (!retVal) {
//                            return false;
//                        }
//                    } catch (Exception ex) {
//                        return false;
//                    }
//                }
//            }
//            return true;
//        } catch (ClassNotFoundException ex) {
//            return false;
//        }

        orgNameValidator(model);
        abbrvValidator(model);
        addrValidator(model);
        allowPubValidator(model);
        emailValidator(model);
        nameValidator(model);
        logoValidator(model);
        phoneNumberValidator(model);
        ukPostcodeValidator(model);
        summaryValidator(model);
        websiteValidator(model);
    } 
    
    private void orgNameValidator(AddOrganisationForm model) {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        
        Query q = em.createNamedQuery("Organisation.findByOrganisationName");
        q.setParameter("organisationName", model.getOrganisation().getOrganisationName());
        
        if (!q.getResultList().isEmpty()) {
            model.addError("Organisation already exists!");
        }
        
        if (model.getOrganisation().getOrganisationName().trim().equals("") || model.getOrganisation().getOrganisationName() != null) {
            model.addError("Need to input an Organisation Name!");
        }
    }
    
    private void abbrvValidator(AddOrganisationForm model) {
        
    }
    
    private void addrValidator(AddOrganisationForm model) {
        
    }
    
    private void allowPubValidator(AddOrganisationForm model) {
        
    }
    
    private void emailValidator(AddOrganisationForm model) {
//        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
//        if (!pattern.matcher(organisation.getContactEmail()).matches()) {
//            model.addError("Invalid E-Mail");
//        }
    }    
    
    private void nameValidator(AddOrganisationForm model) {
        
    }
    
    private void logoValidator(AddOrganisationForm model) {
        
    }
    
    private void phoneNumberValidator(AddOrganisationForm model) {
        Pattern pattern = Pattern.compile("^[0-9]{0,14}$");
        if (!pattern.matcher(model.getOrganisation().getPhone()).matches()) {
            model.addError("Invalid Phonenumber");
        }
    }    
    
    private void ukPostcodeValidator(AddOrganisationForm model) {
        //Pattern pattern = Pattern.compile("^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)$");
        //if (!pattern.matcher(organisation.getPostcode()).matches()) {
        //     model.addError("Invalid Postcode");
        //}
    }
    
    private void summaryValidator(AddOrganisationForm model) {
        
    }
    
    private void websiteValidator(AddOrganisationForm model) {
        
    }
    

    private String base64Image(Byte[] bytes) {
        return null;
    }

}