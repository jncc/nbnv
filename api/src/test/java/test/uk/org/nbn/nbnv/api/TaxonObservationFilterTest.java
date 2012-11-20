/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;

/**
 *
 * @author Paul Gilbertson
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class TaxonObservationFilterTest {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonFilterMapper;
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void create() {
        TaxonObservationFilter f = new TaxonObservationFilter();
        f.setFilterJSON("Test filter JSON");
        f.setFilterText("Test filter text");
        
        oTaxonFilterMapper.createFilter(f);

        Assert.assertTrue(oTaxonFilterMapper.selectAll().size() > 0);
    }

    @Test
    public void createFillsId() {
        TaxonObservationFilter f = new TaxonObservationFilter();
        f.setFilterJSON("Test filter JSON #2");
        f.setFilterText("Test filter text #2");
        
        oTaxonFilterMapper.createFilter(f);

        TaxonObservationFilter f2 = oTaxonFilterMapper.selectById(f.getId());
        
        Assert.assertEquals("Test filter JSON #2", f2.getFilterJSON());
        Assert.assertEquals("Test filter text #2", f2.getFilterText());
    }
}
