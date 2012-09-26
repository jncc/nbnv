/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.metadata.MetadataWriter;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import uk.org.nbn.nbnv.importer.ui.model.UploadItem;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.util.POIImportError;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataFormValidator;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataValidator;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class MetadataController {
    @Autowired SessionData session;
    
    // Limit for wandering up the string list looking for a valid input
    // descriptor contained in the valid stringSet
    private static final int WANDER_MAX = 3;
    
    // Organisation Descriptors
    private static final String ORG_ABBREVIATION = "Abbreviation";
    private static final String ORG_EMAIL = "Contact email";
    private static final String ORG_CONTACT_NAME = "Contact name";
    private static final String ORG_NAME = "Data Provider Name";
    private static final String ORG_LOGO = "Organisation Logo";
    private static final String ORG_DESC = "Organisation Description";
    private static final String ORG_ADDRESS = "Postal Address";
    private static final String ORG_POSTCODE = "Postcode";
    private static final String ORG_PHONE = "Telephone* Number";
    private static final String ORG_WEBSITE = "Website";
    
    // Metadata Descriptors
    private static final String META_ACCESS_CONSTRAINT = "Access Constraint";
    private static final String META_ADDITIONAL_INFO = "Additional Information";
    private static final String META_DATA_CONFIDENCE = "Confidence in the Data";
    private static final String META_DESC = "Description";
    private static final String META_EMAIL = "Email Address";
    private static final String META_GEOCOVER = "Geographical Coverage";
    private static final String META_CAPTURE_METHOD = "Methods of Data Capture";
    private static final String META_NAME = "Name";
    private static final String META_PURPOSE = "Purpose of Data Capture";
    private static final String META_RECORD_ATT = "Record Attributes";
    private static final String META_RECORDERS = "Recorder Names";
    private static final String META_SET_GEORES = "Set Geographic Resolution";
    private static final String META_TITLE = "Title";
    private static final String META_CONTACT_PHONE = "Telephone Number";
    private static final String META_TEMPORAL = "Temporal Coverage";
    private static final String META_USE_CONSTRAINT = "Use Constraint";    
    
    private static final String[] stringsHWPF = {
        ORG_ABBREVIATION, ORG_ADDRESS, ORG_CONTACT_NAME, ORG_DESC, 
        ORG_EMAIL, ORG_LOGO, ORG_NAME, ORG_PHONE, ORG_POSTCODE, 
        ORG_WEBSITE, META_ACCESS_CONSTRAINT, META_ADDITIONAL_INFO, 
        META_CAPTURE_METHOD, META_CONTACT_PHONE, META_DATA_CONFIDENCE,
        META_DESC, META_EMAIL, META_GEOCOVER, META_NAME, META_PURPOSE,
        META_RECORDERS, META_RECORD_ATT, META_SET_GEORES, META_TEMPORAL,
        META_TITLE, META_USE_CONSTRAINT
    };
    
    // NEED TO FIND A WAY TO DEAL WITH CHECKBOXES!
    private static final String[] stringsSpecialAtt = {
        "Set Geographic Resolution*", 
        "Name *", 
        "Record Attributes", 
        "Recorder Names"
    };
    
    // Input form elements for Word doc
    private static final String INPUT_TEXT = "FORMTEXT";
    private static final String INPUT_CHECKBOX = "FORMCHECKBOX";

    @RequestMapping(value="/metadata.html", method = RequestMethod.GET)
    public ModelAndView metadata() {  
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());
        return new ModelAndView("metadataForm", "model", model);
    }
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.GET)
    public ModelAndView metadataProcessGet() {  
        return new ModelAndView("redirect:/metadata.html");
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="addOrganisation")
    public ModelAndView addOrganisation(MetadataForm model, BindingResult result) {
        return new ModelAndView("forward:/addOrganisation.html", "metadataForm", model);
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
    
    @RequestMapping(value="/metadataProcessView.html", method = RequestMethod.POST) 
    public ModelAndView returnViewData (MetadataForm model, BindingResult result, HttpServletRequest request) {
        MetadataForm input = (MetadataForm) request.getAttribute("model");
        input.setOrganisationList(getOrgList());
        return new ModelAndView("metadataForm", "model", input);
    }
    
    @RequestMapping(value="/metadataProcessView.html", method = RequestMethod.GET) 
    public ModelAndView returnViewData () {
        return new ModelAndView("redirect:/metadata.html");
    }

    @RequestMapping(value="/metadata.html", method = RequestMethod.POST)
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
            Map<String, Integer> longDescCutter = new HashMap<String, Integer>();
            longDescCutter.put(META_ACCESS_CONSTRAINT, 15);
            longDescCutter.put(META_DATA_CONFIDENCE, 1);
            longDescCutter.put(META_DESC, 1);
            longDescCutter.put(META_GEOCOVER, 3);
            longDescCutter.put(META_CAPTURE_METHOD, 1);
            longDescCutter.put(META_TEMPORAL, 5);
            longDescCutter.put(META_TITLE, 1);
            
            Map<String, String> testMap = new HashMap<String, String>();
            HashSet<String> stringSet = new HashSet<String>(Arrays.asList(stringsHWPF));
            
            List<String> strList = Arrays.asList(strs);
            
            String desc, str = "", field;
            
            ListIterator<String> strIt = strList.listIterator();
            while (strIt.hasNext()) {
                // Store previous string as possible descriptor

                // Remove stars at end of descriptors if they are there
                desc = str.trim().replaceAll("\\*$", "");
                
                // Store cursor for descriptor
                int descCursor = strIt.previousIndex();
                
                // Get the next string in the iterator
                String origStr = strIt.next();
                // Keep original string for error processing and exception
                // handling
                str = origStr;
                
                // If the str is a FORMTEXT input then we have an input
                // field
                if (str.contains(INPUT_TEXT)) {                    
                    // Store cursor index for next val
                    int cursor = strIt.nextIndex();

                    str = str.replaceAll("^.*" + INPUT_TEXT, "").trim();
                    // Copy over to handle multi-line inputs
                    field = str;
                    
                    // Grab multi-line inputs, might sometimes get, more
                    // than we bargined for, so need to fix these as they
                    // crop up
                    boolean endOfField = false;
                    while(!endOfField) {
                        str = strIt.next();
                        if (str.contains("\r\n")) {
                            field = field + "\n\n" + str.trim();
                        } else {
                            endOfField = true;
                        }
                    }    
                    
                    if (!stringSet.contains(desc.trim())) {
                        // Search back a few entries to see if it just
                        // got lost somewhere
                        strIt = strList.listIterator(descCursor);
                        boolean foundDesc = false;
                        int count = 0;
                        while (count < WANDER_MAX && !foundDesc) {
                            desc = strIt.previous().trim().replaceAll("\\*$", "");
                            if (stringSet.contains(desc.trim())) {
                                foundDesc = true;
                            }
                            count++;
                        }
                        
                        if (!foundDesc) {
                            // Haven't found a valid descriptor so we need
                            // to check our exceptions list
                            if (origStr.contains("I read and understood the NBN Gateway Data Provider Agreement and agree, on behalf of the data provider named in section A, to submit the dataset described in section D to the NBN Trust under this agreement.")) {
                                // Not invalid just the declaration, do some processing here
                            } else {
                                // No exceptions found We have an odity
                                throw new POIImportError("Found an input field with an unknown name input was: " + origStr);
                            }
                        }
                    }
                    
                    // Deal with cases where the descriptions getted tagged
                    // from the metadata 
                    if (longDescCutter.get(desc.trim()) != null && longDescCutter.get(desc.trim()) > 0) {
                        for (int i = 0; i < longDescCutter.get(desc.trim()); i++) {
                            field = field.replaceAll("\n.*$", "");
                        }
                    }
                    
                    testMap.put(desc, field);

                    // Reset iterator to the correct place
                    strIt = strList.listIterator(cursor);
                }
            }
            
            Metadata meta = new Metadata();
            
            meta.setAccess(testMap.get(META_ACCESS_CONSTRAINT));
            meta.setDescription(testMap.get(META_DESC));
            meta.setGeographic(testMap.get(META_GEOCOVER));
            meta.setInfo(testMap.get(META_ADDITIONAL_INFO));
            meta.setMethods(testMap.get(META_CAPTURE_METHOD));
            meta.setPurpose(testMap.get(META_PURPOSE));
            meta.setQuality(testMap.get(META_DATA_CONFIDENCE));
            meta.setTemporal(testMap.get(META_TEMPORAL));
            meta.setTitle(testMap.get(META_TITLE));
            meta.setUse(testMap.get(META_USE_CONSTRAINT));
            
            boolean addOrg = true;
            for (Organisation org : model.getOrganisationList()) {
                if (org.getOrganisationName().equals(testMap.get(ORG_NAME))) {
                    model.getMetadata().setOrganisationID(org.getOrganisationID());
                    addOrg = false;
                }
            }
            
            model.setMetadata(meta);
            
            if (addOrg) {
                Organisation newOrg = new Organisation();
                newOrg.setAbbreviation(testMap.get(ORG_ABBREVIATION));
                newOrg.setAddress(testMap.get(ORG_ADDRESS));
                newOrg.setAllowPublicRegistration(false);
                newOrg.setContactEmail(testMap.get(ORG_EMAIL));
                newOrg.setContactName(testMap.get(ORG_CONTACT_NAME));
                newOrg.setLogo(testMap.get(ORG_LOGO)); // Need to figure out how to import logos
                newOrg.setLogoSmall(testMap.get(ORG_LOGO)); // Need to figure out how to import logos
                newOrg.setOrganisationName(testMap.get(ORG_NAME));
                newOrg.setPhone(testMap.get(ORG_PHONE));
                newOrg.setPostcode(testMap.get(ORG_POSTCODE));
                newOrg.setSummary(testMap.get(ORG_DESC));
                newOrg.setWebsite(testMap.get(ORG_WEBSITE));
                
                ModelAndView mv = new ModelAndView("forward:/addOrganisation.html");
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
    
    @InitBinder("model")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new MetadataFormValidator(new MetadataValidator()));
    }    
    
    private List<Organisation> getOrgList() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        
        Query q = em.createNamedQuery("Organisation.findAll");
        return q.getResultList();
    }
}
