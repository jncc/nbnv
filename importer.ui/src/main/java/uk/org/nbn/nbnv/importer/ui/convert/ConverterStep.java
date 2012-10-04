/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;

/**
 *
 * @author Paul Gilbertson
 */
public interface ConverterStep {
    public String getName();
    public boolean isStepNeeded(List<ColumnMapping> columns);
    public void modifyHeader(List<ColumnMapping> columns);
    public void modifyRow(List<String> row) throws BadDataException;
    public void checkMappings(List<ColumnMapping> columns) throws MappingException;
}
