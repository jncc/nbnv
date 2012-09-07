package uk.org.nbn.nbnv.importer.validation

import org.gbif.dwc.text.Archive
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result
import uk.org.nbn.nbnv.importer.records.NbnRecord

class ArchiveHeadValidator {
  def validate(archive: Archive) = {
    var starRecord = archive.iteratorRaw.next()
    var record = new NbnRecord(starRecord)

    var results = new ListBuffer[Result]

    val v0 = new Nbnv55Validator
    results.append(v0.validate(record))

    val v1 = new Nbnv56Validator
    results.append(v1.validate(record))

    val v2 = new Nbnv57Validator
    results.append(v2.validate(record))

    results.toList
  }
}