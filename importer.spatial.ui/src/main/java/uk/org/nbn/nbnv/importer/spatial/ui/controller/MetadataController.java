package uk.org.nbn.nbnv.importer.spatial.ui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.spatial.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.spatial.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.spatial.ui.model.UploadItem;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.errors.POIImportError;
import uk.org.nbn.nbnv.importer.spatial.ui.util.wordImporter.WordImporter;
import uk.org.nbn.nbnv.importer.spatial.ui.validators.MetadataFormValidator;
import uk.org.nbn.nbnv.importer.spatial.ui.validators.MetadataValidator;
import uk.org.nbn.nbnv.jpa.nbncore.Dataset;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@SessionAttributes({"metadataForm", "org"})
public class MetadataController {
    
    @RequestMapping(value="/metadata.html", method = RequestMethod.GET)
    public ModelAndView metadata() {  
        MetadataForm metadataForm = new MetadataForm();
        metadataForm.updateOrganisationList();
        metadataForm.updateDatasests();
        ModelAndView mv = new ModelAndView("metadataForm", "metadataForm", metadataForm);
        mv.addObject("org", new Organisation());
        return mv;
    }
    
    @RequestMapping(value="/metadata.html", method=RequestMethod.POST, params="updateDataset")
    public ModelAndView updateMetadata(@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation organisation, BindingResult result) {
        metadataForm.resetForm();
        Metadata metadata = metadataForm.getMetadata();
        
        if (!metadata.getDatasetID().equals("")) {
        
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Dataset.findByKey");
            q.setParameter("key", metadata.getDatasetID());
            Dataset dataset = ((List<Dataset>) q.getResultList()).get(0);

            metadata.setTitle(dataset.getTitle());
            metadata.setOrganisationID(dataset.getOrganisation().getId());
            metadata.setDescription(dataset.getDescription());
            metadata.setMethods(dataset.getDataCaptureMethod());
            metadata.setPurpose(dataset.getPurpose());
            metadata.setGeographic(dataset.getGeographicalCoverage());
            metadata.setTemporal(dataset.getTemporalCoverage());
            metadata.setQuality(dataset.getDataQuality());
            metadata.setInfo(dataset.getAdditionalInformation());
            metadata.setDatasetID(dataset.getKey());
            metadata.setDatasetTypeKey(dataset.getDatasetType().getKey());
            metadataForm.setDatasetUpdate(true);
        } else {
            metadataForm.setDatasetError(true);
        }
        
        ModelAndView mv = new ModelAndView("metadataForm", "metadataForm", metadataForm);
        mv.addObject("org", organisation);
        return mv;
    }
    
    @RequestMapping(value="/metadata.html", method=RequestMethod.POST, params="uploadMetadata")
    @SuppressWarnings("static-access")
    public ModelAndView uploadFile(@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation organisation, UploadItem uploadItem, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
            }           
            return new ModelAndView("metadataForm", "metadataForm", metadataForm);
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
            boolean spatial = false;
            while(strIt.hasNext() && version == null) {
                try {
                    String str = strIt.next().trim();
                    if (str.startsWith("Guide to Spatial Datasets")) {
                        spatial = true;
                    }
                    if (str.startsWith("Version ")) {
                        if (spatial) {
                            Pattern pat;
                            pat = Pattern.compile("^([0-9]+)\\.?([0-9]+)?");
                            Matcher matcher = pat.matcher(str.replace("Version ", "").trim());
                            if (matcher.find()) {
                                int major = Integer.parseInt(matcher.group(1));
                                int minor = (matcher.group(2) == null) ? 0 : Integer.parseInt(matcher.group(2));

                                version = Float.parseFloat(Integer.toString(major) + "." + Integer.toString(minor));

                                importer = getDocumentImporter(major, minor);

                                if (importer == null) {
                                   throw new POIImportError("We do not currently support Version " + version + " of the Metadata Import Word Document");
                                }
                            }
                        } else {
                            throw new POIImportError("Could not determine if this document is a spatial metadata import form");
                        }
                    }
                } catch (NumberFormatException ex) {
                    throw new POIImportError("Could not find a valid version number, are you sure this is a metadata import form?");
                }
            }
            
            if (version == null && importer == null) {
                throw new POIImportError("Could not find a version number in the document are you sure this is a metadata import form?");
            }

            List<String> errors = new ArrayList<String>();
            
            Map<String, String> mappings = importer.parseDocument(strList, strIt, new HashMap<String, String>(), errors);
            messages.addAll(importer.getDefaultMessages());
            messages.addAll(errors);
            Metadata meta = new Metadata();
            
            meta.setDescription(mappings.get(importer.META_DESC));
            meta.setGeographic(mappings.get(importer.META_GEOCOVER));
            meta.setInfo(mappings.get(importer.META_ADDITIONAL_INFO));
            meta.setMethods(mappings.get(importer.META_CAPTURE_METHOD));
            meta.setPurpose(mappings.get(importer.META_PURPOSE));
            meta.setQuality(mappings.get(importer.META_DATA_CONFIDENCE));
            meta.setTemporal(mappings.get(importer.META_TEMPORAL));
            meta.setTitle(mappings.get(importer.META_TITLE));
            meta.setDatasetID(metadataForm.getMetadata().getDatasetID());
            
            metadataForm.setMetadata(meta);
            
            if (!((mappings.get(importer.ORG_NAME) == null || mappings.get(importer.ORG_NAME).trim().isEmpty()))) {
                for (Organisation org : metadataForm.getOrganisationList()) {
                    if (org.getName().equals(mappings.get(importer.ORG_NAME))) {
                        meta.setOrganisationID(org.getId());
                        break;
                    }
                }

                if (meta.getOrganisationID() == -1) {
                    organisation = new Organisation();
                    organisation.setAbbreviation(mappings.get(importer.ORG_ABBREVIATION));
                    organisation.setAddress(mappings.get(importer.ORG_ADDRESS));
                    organisation.setAllowPublicRegistration(false);
                    organisation.setContactEmail(mappings.get(importer.ORG_EMAIL));
                    organisation.setContactName(mappings.get(importer.ORG_CONTACT_NAME));
                    //organisation.setLogo(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                    //organisation.setLogoSmall(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                    organisation.setName(mappings.get(importer.ORG_NAME));
                    organisation.setPhone(mappings.get(importer.ORG_PHONE));
                    organisation.setPostcode(mappings.get(importer.ORG_POSTCODE));
                    organisation.setSummary(mappings.get(importer.ORG_DESC));
                    organisation.setWebsite(mappings.get(importer.ORG_WEBSITE));
                    
                    metadataForm.setStoredOrg(true);
                    messages.add("Found an unknown organisation with the name " + organisation.getName() + ", please click Import Organisation to import this as a new Organisation or select the correct Organisation from the drop down list");
                    metadataForm.setErrors(messages);
                    
                    ModelAndView mv = new ModelAndView("metadataForm", "metadataForm", metadataForm);
                    mv.addObject("org", organisation);
                    return mv;
                }
            } else { 
                messages.add("Could not detect Organisation, please select it from the list below or add manually");
            }
            
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception not a valid Word .doc file : " + ex.getMessage());
        } catch (POIImportError ex) {
            messages.add("EXCEPTION: POI Word Parsing exception : " + ex.getMessage());
        } catch (OfficeXmlFileException ex) {
            messages.add("EXCEPTION: POI Word Parsing exception either the supplied file is not a Word .doc file --- Full error is - " + ex.getMessage());
        }
        
        // Return error messages to the interface as well as the data
        metadataForm.setErrors(messages);
        
        return new ModelAndView("metadataForm", "metadataForm", metadataForm);
    }
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.GET)
    public ModelAndView metadataProcessGet() {  
        return new ModelAndView("redirect:/metadata.html");
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="addOrganisation")
    public ModelAndView addOrganisation(@ModelAttribute("metadataForm") MetadataForm metadataForm, BindingResult result) {
        metadataForm.setStoredOrg(false);
        return new ModelAndView("redirect:/organisation.html");
    }
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="importOrganisation")
    public ModelAndView importOrganisation(@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation organisation, BindingResult result) {
        metadataForm.setStoredOrg(true);
        ModelAndView mv = new ModelAndView("redirect:/organisation.html", "org", organisation);
        mv.addObject("metadataForm", metadataForm);
        return mv;
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="submit")
    public ModelAndView uploadFile(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") @Valid MetadataForm metadataForm, BindingResult result, @RequestParam("organisationID") String organisationID, @RequestParam("datasetTypeKey") String datasetTypeKey) {

        metadataForm.getMetadata().setOrganisationID(Integer.parseInt(organisationID));
        metadataForm.getMetadata().setDatasetTypeKey(datasetTypeKey.charAt(0));
        
        if (metadataForm.getMetadata().getOrganisationID() == -404) {
            String[] strs = {"organisationID.required"};
            result.addError(new FieldError("metadataForm", "metadata.organisationID", 
                    "-404", false, strs, null, null));
            // Workaround as the organisation list isn't bound to any value
            metadataForm.setOrgError(true);
        }
        
        if (metadataForm.getMetadata().getDatasetTypeKey() == ' ') {
            String[] strs = {"datasetTypeKey.required"};
            result.addError(new FieldError("metadataForm", "metadata.datasetTypeKey",
                    ' ', false, strs, null, null));
            metadataForm.setSpatialError(true);
        }
        
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                if (error.getDefaultMessage() != null) {
                    metadataForm.getErrors().add(error.getDefaultMessage());
                }
            }
            
            metadataForm.updateOrganisationList();
            metadataForm.resetForm();
            metadataForm.getErrors().add("There are errors with the form, please correct them and re-submit");
            return new ModelAndView("metadataForm", "metadataForm", metadataForm);
        }
        
        organisation = getOrganisationByID(metadataForm.getMetadata().getOrganisationID(), metadataForm.getOrganisationList());
        
        if (metadataForm.getMetadata().getDatasetTypeKey() == 'H') {
            ModelAndView mv = new ModelAndView("redirect:/upsertHabitat.html", "metadataForm", metadataForm);
            mv.addObject("org", organisation);
            return mv;
        } else if (metadataForm.getMetadata().getDatasetTypeKey() == 'S') {
            ModelAndView mv = new ModelAndView("redirect:/upsertSiteBoundary.html", "metadataForm", metadataForm);
            mv.addObject("org", organisation);
            return mv;
        }

        return new ModelAndView("dump", "data", "Unknown dataset type");
    }
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="clearForm")
    public ModelAndView clearForm() {
        return new ModelAndView("redirect:/metadata.html");
    }
     
    @RequestMapping(value="/metadataView.html", method = RequestMethod.GET) 
    public ModelAndView returnViewData (@ModelAttribute("metadataForm") MetadataForm metaDataForm) {
        return new ModelAndView("metadataForm", "metadataForm", metaDataForm);
    }
    
    @RequestMapping(value="/metadataView.html", method = RequestMethod.POST) 
    public ModelAndView returnViewDataPost (@ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("org") Organisation organisation, UploadItem uploadItem, BindingResult result) {
        return uploadFile(metadataForm, organisation, uploadItem, result);
    }

    private WordImporter getDocumentImporter(int major, int minor) {
        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.spatial.ui.util.wordImporter");
        
        Set<Class<? extends WordImporter>> importers = reflections.getSubTypesOf(WordImporter.class);
            
        for (Class<? extends WordImporter> importer : importers) {
            try {
                WordImporter instance = importer.newInstance();    
                if (instance.supports(major, minor)) {
                    return instance;
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    @InitBinder("metadataForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new MetadataFormValidator(new MetadataValidator()));
    }    
    
    private Organisation getOrganisationByID(int id, List<Organisation> orgs) {
        for (Organisation org : orgs) {
            if (org.getId() == id) {
                return org;
            }
        }
        return null;
    }
}