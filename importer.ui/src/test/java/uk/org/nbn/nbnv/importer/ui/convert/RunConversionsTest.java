package uk.org.nbn.nbnv.importer.ui.convert;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.convert.OrganisationStep;
import uk.org.nbn.nbnv.importer.s1.utils.model.MetadataForm;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;

/**
 *
 * @author Matt Debont
 */
public class RunConversionsTest {
    
    public RunConversionsTest() {
             
    }
    
    @Before
    public void setUp() {
        
    }
    
//    // Need to fix, classes are being injected where they shouldn't be for some
//    // reason
//    @Ignore
//    public void testDependencySimple() {
//        ConverterStep[] steps = new ConverterStep[2];
//        steps[0] = new Test1();
//        steps[1] = new Test2();
//        
//        List<ConverterStep> listSteps = Arrays.asList(steps);
//        
//        RunConversions runs;
//        try {
//            runs = new RunConversions(new File(getClass().getClassLoader().getResource("TESTDS02.txt").toURI()), 1, new MetadataForm());
//            runs.setSteps(listSteps);
//            
//            Map<String, String> args = new LinkedHashMap<String, String>();
//            args.put("0", "OCCURRENCEID");
//            args.put("1", "COLLECTIONCODE");
//            args.put("2", "EVENTID");
//            args.put("3", "EVENTDATESTART");
//            args.put("4", "EVENTDATEEND");
//            args.put("5", "EVENTDATETYPECODE");
//            args.put("6", "TAXONID");
//            args.put("7", "SENSITIVEOCCURRENCE");
//            args.put("8", "LOCATIONID");
//            args.put("9", "LOCALITY");
//            args.put("10", "GRIDREFERENCETYPE");
//            args.put("11", "GRIDREFERENCE");
//            args.put("12", "GRIDREFERENCEPRECISION");
//            args.put("13", "RECORDEDBY");
//            args.put("14", "IDENTIFIEDBY");
//            args.put("15", "ATTRIBUTE");
//            args.put("16", "ATTRIBUTE");
//            args.put("17", "ATTRIBUTE");
//            args.put("filename", getClass().getClassLoader().getResource("TESTDS02.txt").toString());
//            
//            File out = File.createTempFile("nbnimporter", "processed.tab");
//            File meta = File.createTempFile("nbnimporter", "meta.xml");
//            
//            runs.run(out, meta, args);
//            
//            listSteps = runs.getSteps();
//
//            assertEquals(listSteps.get(0).getClass(), Test1.class);
//            assertEquals(listSteps.get(1).getClass(), Test2.class);
//        } catch (IOException ex) {
//            Logger.getLogger(RunConversionsTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(RunConversionsTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    
    
    public class Test1 extends ConverterStep {
        public Test1() {
            super(ConverterStep.ADD_COLUMN);
        }
        @Override
        public String getName() { return "Test 1"; }
        @Override
        public boolean isStepNeeded(List<ColumnMapping> columns) { return true; }
        @Override
        public void modifyHeader(List<ColumnMapping> columns) {}
        @Override
        public void modifyRow(List<String> row) throws BadDataException {} 
    }
    
    public class Test2 extends ConverterStep {
        public Test2() {
            super(ConverterStep.ADD_COLUMN);
            this.addDependency(Test1.class);
        }
        @Override
        public String getName() { return "Test 2"; }
        @Override
        public boolean isStepNeeded(List<ColumnMapping> columns) { return true; }
        @Override
        public void modifyHeader(List<ColumnMapping> columns) {}
        @Override
        public void modifyRow(List<String> row) throws BadDataException {} 
    }
    
    public class Test3 extends ConverterStep {
        public Test3() {
            super(ConverterStep.ADD_COLUMN);
            this.addDependency(Test1.class);
            this.addDependency(OrgStep.class);
        }
        @Override
        public String getName() { return "Test 3"; }
        @Override
        public boolean isStepNeeded(List<ColumnMapping> columns) { return true; }
        @Override
        public void modifyHeader(List<ColumnMapping> columns) {}
        @Override
        public void modifyRow(List<String> row) throws BadDataException {} 
    }
    
    public class OrgStep extends OrganisationStep {
        public OrgStep() {
            super(1, ConverterStep.ADD_COLUMN);
            this.addDependency(Test2.class);
        }
        @Override
        public String getName() { return "OrgStep 1"; }
        @Override
        public boolean isStepNeeded(List<ColumnMapping> columns) { return true; }
        @Override
        public void modifyHeader(List<ColumnMapping> columns) {}
        @Override
        public void modifyRow(List<String> row) throws BadDataException {} 
    }
    
}
