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

    val v0 = new Nbnv55Validator
    results.append(v0.validate(record))

    val v1 = new Nbnv56Validator
    results.append(v1.validate(record))

    val v2 = new Nbnv57Validator
    results.append(v2.validate(record))

    val v3 = new Nbnv58Validator
    results.append(v3.validate(record))

    results.toList
  }
}
