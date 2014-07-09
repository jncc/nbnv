package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.errors.NotHarvestedError;
import uk.org.nbn.nbnv.importer.s1.utils.errors.POIImportError;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;

import uk.org.nbn.nbnv.importer.ui.model.UploadItem;
import uk.org.nbn.nbnv.jpa.nbncore.Dataset;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.jpa.nbncore.User;
import uk.org.nbn.nbnv.importer.s1.utils.metadata.harvester.MetadataHarvester;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.model.MetadataForm;
import uk.org.nbn.nbnv.importer.s1.utils.validators.MetadataFormValidator;
import uk.org.nbn.nbnv.importer.s1.utils.validators.MetadataValidator;
import uk.org.nbn.nbnv.importer.s1.utils.wordImporter.WordImporter;

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
        metadata.setGeographicalRes("");
        metadata.setRecordAtts("");
        metadata.setRecorderNames("");
        
        if (!metadata.getDatasetID().equals("")) {
        
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Dataset.findByKey");
            q.setParameter("key", metadata.getDatasetID());
            Dataset dataset = ((List<Dataset>) q.getResultList()).get(0);
            em.close();

            metadata.setTitle(dataset.getTitle());
            metadata.setOrganisationID(dataset.getOrganisation().getId());
            metadata.setDescription(dataset.getDescription());
            metadata.setMethods(dataset.getDataCaptureMethod());
            metadata.setPurpose(dataset.getPurpose());
            metadata.setGeographic(dataset.getGeographicalCoverage());
            metadata.setTemporal(dataset.getTemporalCoverage());
            metadata.setQuality(dataset.getDataQuality());
            metadata.setInfo(dataset.getAdditionalInformation());
            metadata.setUse(dataset.getUseConstraints());
            metadata.setAccess(dataset.getAccessConstraints());

            // Set the Dataset Admin data in the form, if there are any admins
            if (dataset.getUserCollection().size() > 0) {
                User admin = dataset.getUserCollection().iterator().next();
                metadata.setDatasetAdminID(admin.getId());
                metadata.setDatasetAdminName(admin.getForename() + " " + admin.getSurname());
                metadata.setDatasetAdminPhone(admin.getPhone());
                metadata.setDatasetAdminEmail(admin.getEmail());
            }

            // Set the Level of Public Access options  
            if (dataset.getTaxonDataset() != null) {
                if (dataset.getTaxonDataset().getPublicResolution() != null) {
                    if (dataset.getTaxonDataset().getPublicResolution().getId() > 0) {
                        metadata.setGeographicalRes(dataset.getTaxonDataset().getPublicResolution().getAccuracy().toString());
                    } else {
                        metadata.setGeographicalRes("null");
                    }
                }
                
                metadata.setRecordAtts(dataset.getTaxonDataset().getPublicAttribute() ? "true" : "false");
                metadata.setRecorderNames(dataset.getTaxonDataset().getPublicRecorder() ? "true" : "false");
            }
            
            metadata.setDatasetID(dataset.getKey());
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
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
            }           
            return new ModelAndView("metadataForm", "metadataForm", metadataForm);
        }
        
        List<String> messages = new ArrayList<String>();
        
        try {                      
            MetadataHarvester harvester = new MetadataHarvester();
            messages = harvester.harvest(uploadItem.getFileData(), 
                    metadataForm.getMetadata().getDatasetID());
            metadataForm.setMetadata(harvester.getMetadata());
            metadataForm = cleanMetadataTextInputs(metadataForm);
            Map<String, String> mappings = harvester.getMappings();
            WordImporter importer = harvester.getImporter();
            
            if (!((mappings.get(WordImporter.ORG_NAME) == null || mappings.get(WordImporter.ORG_NAME).trim().isEmpty()))) {
                for (Organisation org : metadataForm.getOrganisationList()) {
                    if (org.getName().equals(mappings.get(importer.ORG_NAME))) {
                        metadataForm.getMetadata().setOrganisationID(org.getId());
                        break;
                    }
                }

                if (metadataForm.getMetadata().getOrganisationID() == -1) {
                    organisation = new Organisation();
                    organisation.setAbbreviation(normalizeAndTrim(mappings.get(importer.ORG_ABBREVIATION)));
                    organisation.setAddress(mappings.get(normalizeAndTrim(importer.ORG_ADDRESS)));
                    organisation.setAllowPublicRegistration(false);
                    organisation.setContactEmail(normalizeAndTrim(mappings.get(importer.ORG_EMAIL)));
                    organisation.setContactName(normalizeAndTrim(mappings.get(importer.ORG_CONTACT_NAME)));
                    //organisation.setLogo(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                    //organisation.setLogoSmall(mappings.get(importer.ORG_LOGO)); // Need to figure out how to import logos
                    organisation.setName(normalizeAndTrim(mappings.get(importer.ORG_NAME)));
                    organisation.setPhone(normalizeAndTrim(mappings.get(importer.ORG_PHONE)));
                    organisation.setPostcode(normalizeAndTrim(mappings.get(importer.ORG_POSTCODE)));
                    organisation.setSummary(normalizeAndTrim(mappings.get(importer.ORG_DESC)));
                    organisation.setWebsite(normalizeAndTrim(mappings.get(importer.ORG_WEBSITE)));
                    
                    metadataForm.setStoredOrg(true);
                    messages.add("Found an unknown organisation with the name " + organisation.getName() + ", please click Import Organisation to import this as a new Organisation or select the correct Organisation from the drop down list");
                    metadataForm.setErrors(messages);
                    
                    ModelAndView mv = new ModelAndView("metadataForm", "metadataForm", metadataForm);
                    mv.addObject("org", organisation);
                    return mv;
                    //return new ModelAndView("redirect:/organisation.html", "org", organisation);
                }
            } else { 
                messages.add("Could not detect Organisation, please select it from the list below or add manually");
            }
            
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception not a valid Word .doc file : " + ex.getMessage());
        } catch (POIImportError ex) {
            messages.add("EXCEPTION: POI Word Parsing exception : " + ex.getMessage());
        } catch(NotHarvestedError ex) {
            messages.add("EXCEPTION: Did not harvest word document, document not passed to harvester?");
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
    public ModelAndView uploadFile(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") @Valid MetadataForm metadataForm, BindingResult result, @RequestParam("organisationID") String organisationID) {

        metadataForm = cleanMetadataTextInputs(metadataForm);
        
        metadataForm.getMetadata().setOrganisationID(Integer.parseInt(organisationID));
        
        if (metadataForm.getMetadata().getOrganisationID() == -404) {
            String[] strs = {"organisationID.required"};
            result.addError(new FieldError("metadataForm", "metadata.organisationID", 
                    "-404", false, strs, null, null));
            // Workaround as the organisation list isn't bound to any value
            metadataForm.setOrgError(true);
        }
        
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
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
        
        ModelAndView mv = new ModelAndView("redirect:/upload.html", "metadataForm", metadataForm);
        mv.addObject("org", organisation);
        return mv;
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
    
    @InitBinder("metadataForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new MetadataFormValidator(new MetadataValidator()));
    }    
    
    @ModelAttribute("referenceData")
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, String> geoMap = new LinkedHashMap<String, String>();
        geoMap.put("100","Full");
        geoMap.put("1000","1km\u00B2 ");
        geoMap.put("2000","2km\u00B2");
        geoMap.put("10000","10km\u00B2");
        geoMap.put("null", "No Access");
        
        Map<String, String> recAtts = new LinkedHashMap<String, String>();
        recAtts.put("true","Yes");
        recAtts.put("false","No");
        recAtts.put("null","N/A");
        
        Map<String, String> recNames = new LinkedHashMap<String, String>();
        recNames.put("true","Yes");
        recNames.put("false","No");
        recNames.put("null","N/A");
        
        Map<String, String> insertType = new LinkedHashMap<String, String>();
        insertType.put("new", "New");
        insertType.put("append", "Append");
        insertType.put("update", "Update");
        
        Map<String, Object> ref = new HashMap<String, Object>();
        ref.put("geoMap", geoMap);
        ref.put("recAtts", recAtts);
        ref.put("recNames", recNames);
        ref.put("insertType", insertType);
        
        return ref;
    }    
    
    private Organisation getOrganisationByID(int id, List<Organisation> orgs) {
        for (Organisation org : orgs) {
            if (org.getId() == id) {
                return org;
            }
        }
        return null;
    }
    
    private MetadataForm cleanMetadataTextInputs(MetadataForm metadataForm) {
        
        Metadata metadata = metadataForm.getMetadata();
        
        metadata.setAccess(normalizeAndTrim(metadata.getAccess()));
        metadata.setDatasetAdminEmail(normalizeAndTrim(metadata.getDatasetAdminEmail()));
        metadata.setDatasetAdminName(normalizeAndTrim(metadata.getDatasetAdminName()));
        metadata.setDatasetAdminPhone(normalizeAndTrim(metadata.getDatasetAdminPhone()));
        metadata.setDescription(normalizeAndTrim(metadata.getDescription()));
        metadata.setGeographic(normalizeAndTrim(metadata.getGeographic()));
        metadata.setInfo(normalizeAndTrim(metadata.getInfo().trim()));
        metadata.setMethods(normalizeAndTrim(metadata.getMethods()));
        metadata.setPurpose(normalizeAndTrim(metadata.getPurpose()));
        metadata.setQuality(normalizeAndTrim(metadata.getQuality()));
        metadata.setTemporal(normalizeAndTrim(metadata.getTemporal()));
        metadata.setUse(normalizeAndTrim(metadata.getUse()));
        //metadata.setUse(Normalizer.normalize(metadata.getUse(), Normalizer.Form.NFD).replaceAll("âââââ", "").replaceAll("     ", "").trim()); 
       
        return metadataForm;
    }
    
    private String normalizeAndTrim(String input) {
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFKD).replaceAll("âââââ", "").replaceAll("     ", "");
    }
}