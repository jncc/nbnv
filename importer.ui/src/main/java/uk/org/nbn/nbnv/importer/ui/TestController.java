/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.model.TestData;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class TestController {

    @RequestMapping(value = "/test.html")
    public ModelAndView test() {
        MetaWriter w = new MetaWriter();
        TestData model = new TestData();
        List<ColumnMapping> mapping = new ArrayList<ColumnMapping>();
        mapping.add(new ColumnMapping(0, "occ", DarwinCoreField.OCCURRENCEID));
        mapping.add(new ColumnMapping(1, "loc", DarwinCoreField.LOCALITY));
        mapping.add(new ColumnMapping(2, "rec", DarwinCoreField.RECORDEDBY));
        mapping.add(new ColumnMapping(3, "fed", DarwinCoreField.SENSITIVEOCCURRENCE));
        String output = org.springframework.web.util.HtmlUtils.htmlEscape(w.createMeta(mapping));
        model.setTdata(output);
        return new ModelAndView("test", "model", model);
    }
}
