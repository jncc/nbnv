package uk.org.nbn.nbnv.importer.spatial.ui.controller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uk.org.nbn.nbnv.importer.spatial.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.importer.spatial.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.spatial.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.spatial.ui.validators.AddOrganisationFormValidator;
import uk.org.nbn.nbnv.importer.spatial.ui.validators.OrganisationValidator;
import org.imgscalr.Scalr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
@Controller
@SessionAttributes({"metadataForm", "org"})
public class AddOrganisationController {
    
    private static final int maxLogoWidth = 150;
    private static final int maxLogoSmallWidth = 32;
    private static final String outputType = "png";
    
    private static final String logoDefault = "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";
    private static final String logoSmallDefault = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAadEVYdFNvZnR3YXJlAFBhaW50Lk5FVCB2My41LjEwMPRyoQAAAf5JREFUWEftl7tqAkEUhk2RIkXIe+QJUgZS5hnyEOlTuPECWigIClFUiIg2CqJsomhnoWijiNgYIqjFolFWlCxeTs4McVlD1L25SMjAj+yszP+dM+fMMibTsQyz2XyGukORwRxQD7j2NepEjB0fTlEvqE8UGCAePe6lAFc4IRhgLA3uXQpwa7A5Afn4OwAMw4DL5QKfzwd2u11uDWnLgM1mg3g8DvV6HSaTCSyXSyBjNptBOp2WA6EOwGKxQDabBZ7nqeFqtYLhcAi1Wg2KxSKMRiM6F41G90EoB/B4PNDv90VjEn0gEACyBesiTqVS9H2r1dIfoNPp0MV7vR74/f5fDUql0uEAQqEQZDIZsFqtv5onEglYLBZ0CyKRiP4Z2HZWECCy/8SYKJfL7TPX7xwIh8PAcRxNuyAIkEwm5ZhrB3A6nVCtVsX263a74PV65ZprAyDm4/GYRj2fz6FQKGytix1HvPI2XC8WDAap+XQ6VRq1NEPqAdxuN632ZrOpJOU//6segGSCtKOMVtsFqA1Ah8+3OgBy7MZiMcjn8+BwOIzfApZlaQGSUalUjAdot9siQLlcNh6ApH8wGECj0QByHmioBXU1oMFQ3zbUAeQ/AxsZuNEhpUoLkpPeCy4Q4M1AiDl6PW3ci3HiEvX8fUd8xd9DicW1H1HnR3Ex/wLD9+JphYFTDAAAAABJRU5ErkJggg==";
    
    @RequestMapping(value = "/organisation.html", method = RequestMethod.GET)
    public ModelAndView addOrganisation(@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation org) {       
        AddOrganisationForm orgForm = new AddOrganisationForm();
        if (metadataForm.getStoredOrg()) {
            orgForm.setOrgagnisation(org);
        }
        return new ModelAndView("addOrganisation", "orgForm", orgForm);
    }
        
    @RequestMapping(value="/organisationProcess.html", method=RequestMethod.POST, params="addImage") 
    public ModelAndView addImage(HttpServletRequest request, AddOrganisationForm orgForm, BindingResult result, @RequestParam("imageData") CommonsMultipartFile imageData, @ModelAttribute("org") Organisation org) {
        try {
            if (imageData != null && !imageData.isEmpty()) {               
                ByteArrayInputStream is = new ByteArrayInputStream(imageData.getBytes());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                
                BufferedImage bi = ImageIO.read(is);
                bi = Scalr.resize(bi, maxLogoWidth, Scalr.OP_ANTIALIAS);
                ImageIO.write(bi, outputType, out);
                
                orgForm.getOrganisation().setLogo(out.toByteArray());
                org.setLogo(out.toByteArray());
                
                out = new ByteArrayOutputStream();
                
                bi = Scalr.resize(bi, maxLogoSmallWidth, Scalr.OP_ANTIALIAS);
                ImageIO.write(bi, outputType, out);
                
                orgForm.getOrganisation().setLogoSmall(out.toByteArray());
                org.setLogoSmall(out.toByteArray());
            } else {
                orgForm.setImageError("No Valid Image Selected");
            }
        } catch (IOException ex) {
            Logger.getLogger(AddOrganisationController.class.getName()).log(Level.SEVERE, "Error Processing image", ex);
            orgForm.setImageError("Error Processing image");
        }

        return new ModelAndView("addOrganisation", "orgForm", orgForm);
    }
    
    @RequestMapping(value="/organisationProcess.html", method=RequestMethod.POST, params="submit")
    public ModelAndView processNewOrganisation(@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation org, HttpServletRequest request, @ModelAttribute("orgForm") @Valid AddOrganisationForm orgForm, BindingResult result) {    
        // If we have errors, pass them back to the user, keeping any inputs in 
        // place, need to improve so that errors are displayed next to the
        // correct inputs
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(AddOrganisationController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
            }
            
            return new ModelAndView("addOrganisation", "orgForm", orgForm);
        }
        
        // Pass through the stored logo files if they exist before we kill the organisation object
        orgForm.getOrganisation().setLogo(org.getLogo());
        orgForm.getOrganisation().setLogoSmall(org.getLogoSmall());
        
        org = new Organisation();
        metadataForm.setStoredOrg(false);

        // Write validated organisation to the database
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(orgForm.getOrganisation());
        em.getTransaction().commit();      
        
        metadataForm.getMetadata().setOrganisationID(orgForm.getOrganisation().getId());
        metadataForm.updateOrganisationList();

        return new ModelAndView("redirect:/metadataView.html", "metadataForm", metadataForm);
    }
    
    @RequestMapping(value="/imageBase/{type}")
    @ResponseBody
    public ResponseEntity<byte[]> getImgBase(@PathVariable String type, @ModelAttribute("org") Organisation org) {
        Base64 in = new Base64(true);
        byte[] out = {};
        if (type.equals("large")) {
            if (org.getLogo() != null && org.getLogo().length > 0) {
                out = org.getLogo();
            } else {
                out = in.decode(logoDefault);
            }
        } else if (type.equals("small")) {
            if (org.getLogoSmall() != null && org.getLogoSmall().length > 0) {
                out = org.getLogoSmall();
            } else {
                out = in.decode(logoSmallDefault);
            }
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<byte[]>(out, headers, HttpStatus.CREATED);
    }    
    
    @InitBinder("orgForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new AddOrganisationFormValidator(new OrganisationValidator()));
    }
}