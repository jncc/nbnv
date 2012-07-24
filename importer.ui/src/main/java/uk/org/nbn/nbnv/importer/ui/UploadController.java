/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.model.UploadItem;
import uk.org.nbn.nbnv.importer.ui.model.UploadItemResults;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;

/**
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/upload.html")
public class UploadController {
    @RequestMapping(method = RequestMethod.GET)
    public String upload() {
        return "upload";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView uploadFile(UploadItem uploadItem, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
            }
            return new ModelAndView("upload");
        }
        
        List<String> messages = new ArrayList<String>();
        messages.add("Input name: " + uploadItem.getName());
        messages.add("Original File name: " + uploadItem.getFileData().getOriginalFilename());
        messages.add("File size: " + Long.toString(uploadItem.getFileData().getSize()));
        messages.add("Content Type: " + uploadItem.getFileData().getContentType());
        messages.add("Storage description: " + uploadItem.getFileData().getStorageDescription());

        UploadItemResults model = new UploadItemResults();

        try {
            NXFParser parser = new NXFParser();
            model.setHeaders(parser.parseHeaders(uploadItem.getFileData().getFileItem()));
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception: " + ex.getMessage());
        }

        model.setResults(messages);
        model.setFields(Arrays.asList(DarwinCoreField.values()));
        
        return new ModelAndView("uploadSuccess", "model", model);
    }

}
