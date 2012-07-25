/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.TemplateModelException;
import javax.servlet.ServletException;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;

/**
 *
 * @author Administrator
 */
public class PowerlessServlet extends FreemarkerServlet{
    @Override
    public void init() throws ServletException {
        try {
            super.init();
            getConfiguration().setSharedVariable("markdown", new MarkDownDirectiveModel());
            getConfiguration().setSharedVariable("json", new JSONReaderForFreeMarker());
        } catch (TemplateModelException ex) {
            throw new ServletException(ex);
        }
    }
}
