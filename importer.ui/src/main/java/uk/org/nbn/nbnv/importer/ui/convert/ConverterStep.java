/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.List;
import java.util.Map;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public interface ConverterStep {
    public String getName();
    public boolean isStepNeeded(List<ColumnMapping> columns);
    public void modifyRow(Map<DarwinCoreField, String> row) throws BadDataException;
}
