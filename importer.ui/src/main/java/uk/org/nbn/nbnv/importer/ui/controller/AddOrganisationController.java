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
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Encoder;
import uk.org.nbn.nbnv.importer.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.validators.AddOrganisationFormValidator;
import uk.org.nbn.nbnv.importer.ui.validators.OrganisationValidator;


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
                Logger.getLogger(AddOrganisationController.class.getName()).log(Level.SEVERE, "Error Processing image", ex);
                model.addError("Error Processing image");
            }
        }
        
        return new ModelAndView("addOrganisation", "model", model);
    }
    
    @RequestMapping(value="/addOrganisationProcess.html", method=RequestMethod.POST, params="submit")
    public ModelAndView processNewOrganisation(@Valid AddOrganisationForm model, BindingResult result) {    
        // If we have errors, pass them back to the user, keeping any inputs in 
        // place, need to improve so that errors are displayed next to the
        // correct inputs
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.addErrors(error.getCodes());
            }
            return new ModelAndView("addOrganisation", "model", model);
        }
        
        // Write validated organisation to the database
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(model.getOrganisation());
        em.getTransaction().commit();            

        // Return to metadata input form, should probably find a way of auto-selecting the new organisation
        return new ModelAndView("redirect:/metadata.html", "model", new MetadataForm());        
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new AddOrganisationFormValidator(new OrganisationValidator()));
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
     * height / width, probably should do this using jQuery or some image library
     * client side rather than server side, plus this code is quick and crappy
     * 
     * @param bi The original image to be encoded
     * @param maxWidth The maximum width of the image
     * @param maxHeight The maximum height of the image
     * @param model The model to return any errors and foul-ups
     * @return 
     */
    private String generateBase64EncodedImage(BufferedImage bi, int maxWidth, int maxHeight, AddOrganisationForm model) {
        try {
            Size size = fitBoundingBox(bi.getHeight(), bi.getWidth(), maxHeight, maxWidth);
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

class Size {
    private int height;
    private int width;
    
    public Size(int height, int width) {
        this.height = height;
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }    
}