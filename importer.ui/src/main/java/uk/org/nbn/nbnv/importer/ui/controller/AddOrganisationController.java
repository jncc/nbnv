/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Encoder;
import uk.org.nbn.nbnv.importer.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.util.Size;

/**
 *
 * @author Matt Debont
 */
@Controller
public class AddOrganisationController {
    @Autowired SessionData sessionData;
    
    private static final int maxLogoWidth = 150;
    private static final int maxLogoHeight = 150;
    private static final int maxLogoSmallWidth = 32;
    private static final int maxLogoSmallHeight = 32;
    private static final String outputType = "png";
    private static final int maxSize = 60000; // probably a little high
    
    @RequestMapping(value="/addOrganisation.html", method=RequestMethod.GET)
    public ModelAndView addOrganisation() {
       AddOrganisationForm model = new AddOrganisationForm();
       return new ModelAndView("addOrganisation", "model", model);
    }   
    
    @RequestMapping(value="/addOrganisationProcess.html", method=RequestMethod.POST, params="addImage") 
    public ModelAndView addImage(AddOrganisationForm model, BindingResult result, @RequestParam("imageData") CommonsMultipartFile imageData) {
        if (imageData.getSize() > maxSize) {
            model.addError("Provided image is to large!");
        } else {
            String logoPrefix = "data:" + imageData.getContentType().toString() + ";base64,";

            try {
                ByteArrayInputStream is = new ByteArrayInputStream(imageData.getBytes());
                BufferedImage bi = ImageIO.read(is);

                model.getOrganisation().setLogo(logoPrefix + generateBase64EncodedImage(bi, maxLogoWidth, maxLogoHeight, model));
                model.getOrganisation().setLogoSmall(logoPrefix + generateBase64EncodedImage(bi, maxLogoSmallWidth, maxLogoSmallHeight, model));

            } catch (IOException ex) {
                Logger.getLogger(AddOrganisationController.class.getName()).log(Level.SEVERE, null, ex);
                model.addError("Error Processing image");
            }
        }
        
        return new ModelAndView("addOrganisation", "model", model);
    }
    
    @RequestMapping(value="/addOrganisationProcess.html", method=RequestMethod.POST, params="submit")
    public ModelAndView processNewOrganisation(AddOrganisationForm model, BindingResult result) {
        
        // If we have errors, pass them back to the user, keeping any inputs in 
        // place, need to improve so that errors are displayed next to the
        // correct inputs
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }
            return new ModelAndView("addOrganisation", "model", model);
        }
        
        // Run Validators to give a sanity check on the data and hopefully 
        // ensure no wierdness
        runValidators(model);
        
        // If we have no errors then attempt to push the new organisation in the 
        // database, otherwise push errors back to the user
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
    
    /**
     * Run all of the validation methods in the OrganisationValidator Validation
     * class, currently under development but currently runs any method in this
     * object of the form "*Validator(AddOrganisationForm model)" and tots up
     * errors
     * 
     * @param model Model containing all data to be validated for the organisation
     * to be added
     */
    private void runValidators(AddOrganisationForm model) {

        Class cls = this.getClass();
        Method methodList[] = cls.getDeclaredMethods();

        Pattern pattern = Pattern.compile("(a-zA-Z)*Validator$");

        for (Method method : methodList) {
            if (pattern.matcher(method.getName()).matches()) {
                try {
                    method.invoke(model);
                } catch (Exception ex) {
                    model.addError("Error occured while attempting to reflect validators");
                }
            }
        }
        
//        orgNameValidator(model);
//        abbrvValidator(model);
//        addrValidator(model);
//        allowPubValidator(model);
//        emailValidator(model);
//        nameValidator(model);
//        logoValidator(model);
//        phoneNumberValidator(model);
//        ukPostcodeValidator(model);
//        summaryValidator(model);
//        websiteValidator(model);
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
    
    // ********************************************************************
    // Image Manipulation functions for logo resizing
    // ********************************************************************
    
    /**
     * Figure out the new size of a supplied image, shrinks or grows the 
     * supplied image giving the new width / height value given the bounding
     * box and width / height parameters
     * 
     * @param height The height of the supplied image
     * @param width The width of the supplied image
     * @param maxHeight The height of the bounding box
     * @param maxWidth The width of the bounding box
     * @return A Size variable containing a height and width for the new image 
     */
    private Size fitBoundingBox(int height, int width, int maxHeight, int maxWidth) {
        int newHeight = height;
        int newWidth = width;
        
        float aspectRatio = (float)width / (float)height;
        
        if (newWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) (newWidth / aspectRatio);
        }
        
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }
        
        return new Size(newHeight, newWidth);
    }
    
    /**
     * Resize a given image to the given sizes and return that image
     * 
     * @param originalImage The Original image to process
     * @param scaledWidth The new image width
     * @param scaledHeight The new image height
     * @param preserveAlpha If we should preserve the alpha channel (which we should)
     * @return 
     */
    private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }        
        
    /**
     * Generate a base64 encoded string form of any given image, given a max 
     * height / width
     * 
     * @param bi The original image to be encoded
     * @param maxWidth The maximum width of the image
     * @param maxHeight The maximum height of the image
     * @param model The model to return any errors and foul-ups
     * @return 
     */
    private String generateBase64EncodedImage(BufferedImage bi, int maxWidth, int maxHeight, AddOrganisationForm model) {
        try {
            Size size = fitBoundingBox(bi.getHeight(), bi.getWidth(), maxLogoHeight, maxLogoWidth);
            BufferedImage re = createResizedCopy(bi, size.getWidth(), size.getHeight(), true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            ImageIO.write(re, outputType, baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            return new BASE64Encoder().encode(imageInByte);
            
        } catch (IOException ex) {
            Logger.getLogger(AddOrganisationController.class.getName()).log(Level.SEVERE, null, ex);
            model.addError("Error Processing image");
        }
        
        return "";
    }        
}