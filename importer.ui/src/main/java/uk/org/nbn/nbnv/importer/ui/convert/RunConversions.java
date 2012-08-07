/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {

    public List<ConverterStep> getSteps(List<ColumnMapping> mappings) {
        List<ConverterStep> steps = new ArrayList<ConverterStep>();

        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.convert.converters");
        Set<Class<? extends ConverterStep>> converters = reflections.getSubTypesOf(ConverterStep.class);

        for (Class<? extends ConverterStep> stepClass : converters) {
            try {
                ConverterStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    steps.add(step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return steps;
    }

    public void modifyColumns(List<ConverterStep> steps, List<ColumnMapping> mappings) {
        for (ConverterStep step : steps) {
            step.modifyHeader(mappings);
        }
    }

    public void modifyRow(List<ConverterStep> steps, List<String> row) throws BadDataException {
        for (ConverterStep step : steps) {
            step.modifyRow(row);
        }
    }

    public List<String> run(File in, File out, File meta, Map<String, String> args) throws IOException {
        List<String> errors = new ArrayList<String>();
        BufferedWriter w = null;
        
        try {
            w = new BufferedWriter(new FileWriter(out));

            NXFParser nxfParser = new NXFParser(in);
            List<ColumnMapping> mappings = nxfParser.parseHeaders();

            for (ColumnMapping cm : mappings) {
                if (args.containsKey(Integer.toString(cm.getColumnNumber()))) {
                    cm.setField(DarwinCoreField.valueOf(args.get(Integer.toString(cm.getColumnNumber()))));
                }
            }

            RunConversions rc = new RunConversions();
            List<ConverterStep> steps = rc.getSteps(mappings);
            rc.modifyColumns(steps, mappings);
            List<String> columnNames = new ArrayList<String>();

            for (ColumnMapping cm : mappings) {
                columnNames.add(cm.getColumnLabel());
            }

            w.write(StringUtils.collectionToDelimitedString(columnNames, "\t"));
            w.newLine();

            List<String> row;
            while ((row = nxfParser.readDataLine()) != null) {
                try {
                    rc.modifyRow(steps, row);
                } catch (BadDataException ex) {
                    errors.add("Bad Data: " + ex.getMessage());
                }
                w.write(StringUtils.collectionToDelimitedString(row, "\t"));
                w.newLine();
            }
            
            MetaWriter mw = new MetaWriter();
            errors.addAll(mw.createMetaFile(mappings, meta));
        } catch (IOException ex) {
            errors.add("IOException: " + ex.getMessage());
        } finally {
            if (w != null) {
                w.close();
            }
        }
        return errors;
    }
}
