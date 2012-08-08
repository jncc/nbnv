/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@RequestMapping("/download.html")
public class DownloadController {
    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("file") == null) {
            return new ModelAndView("exception", "error", "No file argument");
        }
        
        String file = request.getParameter("file");
        
        if (!file.endsWith("archive.zip")) {
            return new ModelAndView("exception", "error", "Not a DwC-A archive");
        }
        
        File f = new File(file);
        response.setContentType("application/zip");
        response.setContentLength((int)f.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"archive.zip\"");
        
        FileCopyUtils.copy(new FileInputStream(f), response.getOutputStream());
        
        return null;
    }
}
