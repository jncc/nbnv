/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.mvc.SimpleFormController;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;
import uk.org.nbn.nbnv.importer.ui.metadata.MetadataWriter;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import uk.org.nbn.nbnv.importer.ui.model.UploadItem;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.util.POIImportError;
import uk.org.nbn.nbnv.importer.ui.util.wordImporter.WordImporter;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataFormValidator;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataValidator;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.jpa.nbncore.UserData;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class MetadataController {
    @Autowired SessionData session;
    
    @RequestMapping(value="/metadata.html", method = RequestMethod.GET)
    public ModelAndView metadata() {  
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());
        ModelAndView mv = new ModelAndView("metadataForm", "model", model);
        return mv;
    }
    
    @RequestMapping(value="/metadata.html", method = RequestMethod.POST)
    @SuppressWarnings("static-access")
    public ModelAndView uploadFile(UploadItem uploadItem, BindingResult result) {
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }           
            return new ModelAndView("metadataForm", "model", model);
        }

        List<String> messages = new ArrayList<String>();
        
        try {           
            HWPFDocument doc = new HWPFDocument(uploadItem.getFileData().getInputStream());
            WordExtractor ext = new WordExtractor(doc);
            String[] strs = ext.getParagraphText();
            
            // Catch some of the annoying cases where inputs are followed by
            // paragraphs of text, as we are using \r\n to seperate out the 
            // inputs

            List<String> strList = Arrays.asList(strs);
            ListIterator<String> strIt = strList.listIterator();
            
            // Check the version number of the document to make sure we 
            // can read it
            Float version = null;
            WordImporter importer = null;
            while(strIt.hasNext() && version == null) {
                try {
                    String str = strIt.next();
                    if (str.startsWith("Version ")) {
                        Pattern pat;
                        pat = Pattern.compile("([0-9]+)\\.([0-9]+)");
                        Matcher matcher = pat.matcher(str);
                        if (matcher.find()) {
                            int major = Integer.parseInt(matcher.group(1));
                            int minor = Integer.parseInt(matcher.group(2));

                            version = Float.parseFloat(Integer.toString(major) + "." + Integer.toString(minor));

                            importer = getDocumentImporter(major, minor);

                            if (importer == null) {
                               throw new POIImportError("We do not currently support Version " + version + " of the Metadata Import Word Document");
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    throw new POIImportError("Could not find a valid version number, are you sure this is a metadata import form?");
                }
            }

            Map<String, String> mappings = importer.parseDocument(strList, strIt, new HashMap<String, String>());
            
            Metadata meta = new Metadata();
            
            meta.setAccess(mappings.get(importer.META_ACCESS_CONSTRAINT));
            meta.setDescription(mappings.get(importer.META_DESC));
            meta.setGeographic(mappings.get(importer.META_GEOCOVER));
            meta.setInfo(mappings.get(importer.META_ADDITIONAL_INFO));
            meta.setMethods(mappings.get(importer.META_CAPTURE_METHOD));
            meta.setPurpose(mappings.get(importer.META_PURPOSE));
            meta.setQuality(mappings.get(importer.META_DATA_CONFIDENCE));
            meta.setTemporal(mappings.get(importer.META_TEMPORAL));
            meta.setTitle(mappings.get(importer.META_TITLE));
            meta.setUse(mappings.get(importer.META_USE_CONSTRAINT));
            meta.setDatasetAdminName(mappings.get(importer.META_NAME));
            meta.setDatasetAdminPhone(mappings.get(importer.META_CONTACT_PHONE));
            meta.setDatasetAdminEmail(mappings.get(importer.META_EMAIL));
            
            boolean addOrg = true;
            for (Organisation org : model.getOrganisationList()) {
                if (org.getOrganisationName().equals(mappings.get(importer.ORG_NAME))) {
                    meta.setOrganisationID(org.getOrganisationID());
                    addOrg = false;
                }
            }
            
            
            
            model.setMetadata(meta);
            
            if (addOrg) {
                Organisation newOrg = new Organisation();
                newOrg.setAbbreviation(mappings.get(importer.ORG_ABBREVIATION));
                newOrg.setAddress(mappings.get(importer.ORG_ADDRESS));
                newOrg.setAllowPublicRegistration(false);
                newOrg.setContactEmail(mappings.get(importer.ORG_EMAIL));
                newOrg.setContactName(mappings.get(importer.ORG_CONTACT_NAME));
                newOrg.setLogo(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                newOrg.setLogoSmall(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                newOrg.setOrganisationName(mappings.get(importer.ORG_NAME));
                newOrg.setPhone(mappings.get(importer.ORG_PHONE));
                newOrg.setPostcode(mappings.get(importer.ORG_POSTCODE));
                newOrg.setSummary(mappings.get(importer.ORG_DESC));
                newOrg.setWebsite(mappings.get(importer.ORG_WEBSITE));
                
                ModelAndView mv = new ModelAndView("forward:/organisation/add.html");
                mv.addObject("metadataForm", model);
                mv.addObject("newOrganisation", newOrg);
                return mv;
            }
            
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception: " + ex.getMessage());
        } catch (POIImportError ex) {
            messages.add("EXCEPTION: POI Word Parsing exception: " + ex.getMessage());
        }
        
        // Return error messages to the interface as well as the data
        model.setErrors(messages);
        
        return new ModelAndView("metadataForm", "model", model);
    }
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.GET)
    public ModelAndView metadataProcessGet() {  
        return new ModelAndView("redirect:/metadata.html");
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="addOrganisation")
    public ModelAndView addOrganisation(MetadataForm model, BindingResult result) {
        return new ModelAndView("forward:/organisation.html", "metadataForm", model);
    }    
    
    private Organisation getOrganisationByID(int organisationID, List<Organisation> organisations) {
        for (Organisation org : organisations) {
            if (org.getOrganisationID() == organisationID) {
                return org;
            }
        }
        return null;
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="submit")
    public ModelAndView uploadFile(@ModelAttribute("model") @Valid MetadataForm model, BindingResult result, @RequestParam("organisationID") String organisationID) {

        // Quick fix to grab the ID of the organisation and push it in to the 
        // model for processing or return to user to ensure correct option is 
        // re-selected 
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
            model.getMetadata().setOrganisationID(nf.parse(organisationID).intValue());
        } catch (ParseException ex) {
            Logger.getLogger(UploadController.class.getName()).log(Level.SEVERE, "Error ({0}): {1}", new Object[]{"ParsingError", "Could Not Parse Selected Organisation ID"});
            model.getErrors().add("Could Not Parse Selected Organisation ID");
            
            return new ModelAndView("metadataForm", "model", model);
        }
        
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }
            
            model.setOrganisationList(getOrgList());
            
            return new ModelAndView("metadataForm", "model", model);
        }


        try {
            File metadataFile = File.createTempFile("nbnimporter", "metadata.xml");
            MetadataWriter mw = new MetadataWriter(metadataFile);
            mw.datasetToEML(model.getMetadata());
            
            session.setMetadata(metadataFile.getAbsolutePath());
            session.setOrganisationID(model.getMetadata().getOrganisationID());
            
            return new ModelAndView("upload");
        } catch (Exception ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            model.getErrors().add(ex.getMessage());
        }

        return new ModelAndView("metadataForm", "model", model);
    }
    
    @RequestMapping(value="/metadataView.html", method = RequestMethod.POST) 
    public ModelAndView returnViewData (MetadataForm model, BindingResult result, HttpServletRequest request) {
        MetadataForm input = (MetadataForm) request.getAttribute("model");
        input.setOrganisationList(getOrgList());
        return new ModelAndView("metadataForm", "model", input);
    }
    
    @RequestMapping(value="/metadataView.html", method = RequestMethod.GET) 
    public ModelAndView returnViewData () {
        return new ModelAndView("redirect:/metadata.html");
    }

    private WordImporter getDocumentImporter(int major, int minor) {
        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.util.wordImporter");
        
        Set<Class<? extends WordImporter>> importers = reflections.getSubTypesOf(WordImporter.class);
            
        for (Class<? extends WordImporter> importer : importers) {
            try {
                WordImporter instance = importer.newInstance();    
                if (instance.supports(major, minor)) {
                    return instance;
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    @InitBinder("model")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new MetadataFormValidator(new MetadataValidator()));
    }    
    
    @ModelAttribute("referenceData")
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, String> geoMap = new LinkedHashMap<String, String>();
        geoMap.put("full","Full");
        geoMap.put("1","1km^2");
        geoMap.put("2","2km^2");
        geoMap.put("10","10km^2");
        
        Map<String, String> recAtts = new LinkedHashMap<String, String>();
        recAtts.put("yes","Yes");
        recAtts.put("no","No");
        recAtts.put("na","N/A");
        
        Map<String, String> recNames = new LinkedHashMap<String, String>();
        recNames.put("yes","Yes");
        recNames.put("no","No");
        recNames.put("na","N/A");
        
        Map<String, Object> ref = new HashMap<String, Object>();
        ref.put("geoMap", geoMap);
        ref.put("recAtts", recAtts);
        ref.put("recNames", recNames);
        
        return ref;
    }    
    
    private List<Organisation> getOrgList() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        
        Query q = em.createNamedQuery("Organisation.findAll");
        return q.getResultList();
    }
}