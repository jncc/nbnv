package uk.org.nbn.nbnv.importer.validation

import org.gbif.dwc.text.Archive
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result
import uk.org.nbn.nbnv.importer.records.NbnRecord

class ArchiveHeadValidator {
  def validate(archive: Archive) = {
    val starRecord = archive.iteratorRaw.next()
    val record = new NbnRecord(starRecord)

    val results = new ListBuffer[Result]

    //Validate RecordKey exists
    val v0 = new Nbnv55Validator
    results.append(v0.validate(record))

    //Validate TaxonVerionKey exits
    val v1 = new Nbnv56Validator
    results.append(v1.validate(record))

    //validate dates fields have been defined
    val v2 = new Nbnv57Validator
    results.append(v2.validate(record))

    //validate a location fields have been defined
    val v3 = new Nbnv58Validator
    results.append(v3.validate(record))

    results.toList
  }
}
