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
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    
    private static final String[] stringsHWPF = {
        "Abbreviation", "Access Constraint", "Additional Information", 
        "Confidence in the Data", "Contact email", "Contact name", 
        "Data Provider Name", "Description", "Email Address", 
        "Geographical Coverage", "Methods of Data Capture", "Name", 
        "Organisation Logo", "Organisation Description", "Postal Address", 
        "Postcode", "Purpose of Data Capture", "Record Attributes", 
        "Recorder Names", "Set Geographic Resolution", "Title", "Telephone Number", 
        "Telephone* Number", "Temporal Coverage", "Use Constraint", "Website"
    };
    
    private static final String[] stringsSpecialAtt = {
        "Set Geographic Resolution*", 
        "Name *", 
        "Record Attributes", 
        "Recorder Names"
    };
    
    private static final String[] operators = {
        "FORMTEXT", "FORMCHECKBOX"
    };
    
    // Catch some of the annoying cases where inputs are followed by
    // paragraphs of text, as we are using \r\n to seperate out the 
    // inputs
    private String[] longDescriptions = {
        "Access Constraint", "Confidence in the Data", "Description", 
        "Geographical Coverage", "Methods of Data Capture", 
        "Temporal Coverage", "Title"
    };
    
    @RequestMapping(value="/metadata.html", method = RequestMethod.GET)
    public ModelAndView metadata() {  
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());
        return new ModelAndView("metadataForm", "model", model);
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="addOrganisation")
    public ModelAndView addOrganisation(Metadata metadata, BindingResult result) {
        return new ModelAndView("redirect:/addOrganisation.html");
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
        messages.add("Original File name: " + uploadItem.getFileData().getOriginalFilename());
        messages.add("File size: " + Long.toString(uploadItem.getFileData().getSize()));
        messages.add("Content Type: " + uploadItem.getFileData().getContentType());
        messages.add("Storage description: " + uploadItem.getFileData().getStorageDescription());

        
        try {
            //File dFile = File.createTempFile("nbnimporter", "metadata.doc");
            //messages.add("Storage location: " + dFile.getAbsolutePath());
            //uploadItem.getFileData().transferTo(dFile);
            
            HWPFDocument doc = new HWPFDocument(uploadItem.getFileData().getInputStream());
            WordExtractor ext = new WordExtractor(doc);
            String[] strs = ext.getParagraphText();
            
            Map<String, String> testMap = new HashMap<String, String>();
            HashSet<String> stringSet = new HashSet<String>(Arrays.asList(stringsHWPF));
            HashSet<String> longDesc = new HashSet<String>(Arrays.asList(longDescriptions));
            
            List<String> strList = Arrays.asList(strs);
            
            String desc, str = "", field;
            
            ListIterator<String> strIt = strList.listIterator();
            while (strIt.hasNext()) {
                // Store previous string as possible descriptor

                desc = str.trim().replaceAll("\\*$", "");
                // Store cursor for descriptor
                int descCursor = strIt.previousIndex();
                // Get the next string in the iterator
                String origStr = strIt.next();
                str = origStr;
                
                // If the str is a FORMTEXT input then we have an input
                // field
                if (str.contains("FORMTEXT")) {                    
                    // Store cursor index for next val
                    int cursor = strIt.nextIndex();

                    str = str.replaceAll("^.*FORMTEXT", "").trim();
                    // Copy over to handle multi-line inputs
                    field = str;
                    
                    // Grab multi-line inputs
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
                            System.out.println(origStr);
                            if (origStr.contains("I read and understood the NBN Gateway Data Provider Agreement and agree, on behalf of the data provider named in section A, to submit the dataset described in section D to the NBN Trust under this agreement.")) {
                                // Not invalid just the declaration, do some processing here
                            } else {
                                // No exceptions found We have an odity
                                throw new POIImportError("Found an input field with an unknown name input was: " + str);
                            }
                        }
                    }
                    
                    if (longDesc.contains(desc.trim())) {
                        field = field.replaceAll("\n.*$", "");
                        
                        // Oddities and annoyances
                        if (desc.trim().equals("Geographical Coverage")) {
                            field = field.replaceAll("\n.*\n.*$", "");
                        }
                    }
                    
                    testMap.put(desc, field);

                    // Reset iterator to the correct place
                    strIt = strList.listIterator(cursor);
                }
            }
            
            Metadata meta = new Metadata();
            
            meta.setAccess(testMap.get("Access Constraint"));
            meta.setDescription(testMap.get("Description"));
            meta.setGeographic(testMap.get("Geographical Coverage"));
            meta.setInfo(testMap.get("Additional Information"));
            meta.setMethods(testMap.get("Methods of Data Capture"));
            meta.setPurpose(testMap.get("Purpose of Data Capture"));
            meta.setQuality(testMap.get("Confidence in the Data"));
            meta.setTemporal(testMap.get("Temporal Coverage"));
            meta.setTitle(testMap.get("Title"));
            meta.setUse(testMap.get("Use Constraint"));

            model.setMetadata(meta);
            
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception: " + ex.getMessage());
        } catch (POIImportError ex) {
            messages.add("EXCEPTION: POI Word Parsing exception: " + ex.getMessage());
        }
        
        
        
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
