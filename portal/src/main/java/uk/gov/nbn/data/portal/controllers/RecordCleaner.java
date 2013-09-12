/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Matt Debont
 */
@Controller
public class RecordCleaner {
    @Autowired WebResource resource; 
    @Autowired ServletContext servletContext;
    
    @RequestMapping(value = "/recordcleaner/application", method = RequestMethod.GET)
    public void checkUpdates(HttpServletResponse response) throws IOException {
        String b = servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\application\\RecordCleaner.xml";
        response.setContentType("application/xml");
        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(new File(servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\application\\RecordCleaner.xml"));
        IOUtils.copy(is, os);
        response.flushBuffer();
    }

    /**
     * Returns the record cleaner application
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/recordcleaner/application/NBNRecordCleaner.exe", method = RequestMethod.GET)
    public void getApplication(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(new File(servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\application\\NBNRecordCleaner.exe"));
        IOUtils.copy(is, os);
        response.flushBuffer();
    }

    @RequestMapping(value = "/recordcleaner/rules/servers.txt", method = RequestMethod.GET)
    public void requestRules(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/pdf");
            OutputStream os = response.getOutputStream();
            InputStream is = new FileInputStream(new File(servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\rules\\servers.txt"));
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch(FileNotFoundException ex) {
            response.setStatus(404);
        }        
    }

    @RequestMapping(value = "/recordcleaner/rules/{rule}/{file}", method = RequestMethod.GET)
    public void requestRuleFile(
            @PathVariable String rule,
            @PathVariable String file,
            HttpServletResponse response) throws IOException {
        try {
            if (file.substring(file.length() - 4).equals(".zip")) {
                response.setContentType("application/zip");
            } else if (file.substring(file.length() - 4).equals(".txt")) {
                response.setContentType("application/txt");
            } else {
                response.setStatus(404);
                return;
            }
            OutputStream os = response.getOutputStream();
            InputStream is = new FileInputStream(new File(servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\rules\\" + rule + "\\" + file));
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch(FileNotFoundException ex) {
            response.setStatus(404);
        }  
    }

    @RequestMapping(value = "/recordcleaner/documentation/{file}", method = RequestMethod.GET)
    public void requestDocumentation(@PathVariable String file, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/pdf");
            OutputStream os = response.getOutputStream();
            InputStream is = new FileInputStream(new File(servletContext.getRealPath("/WEB-INF/recordcleaner") + "\\documentation\\" + file));
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch(FileNotFoundException ex) {
            response.setStatus(404);
        }
    }
}
