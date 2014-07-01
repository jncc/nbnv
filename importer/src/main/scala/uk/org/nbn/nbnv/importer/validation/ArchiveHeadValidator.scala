package uk.org.nbn.nbnv.importer.validation

import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result
import uk.org.nbn.nbnv.importer.archive.Archive

class ArchiveHeadValidator {
  def validate(archive: Archive) = {

    val metadata = archive.getArchiveMetadata()

    val results = new ListBuffer[Result]



    //Validate RecordKey exists
    val v0 = new Nbnv55Validator
    results.append(v0.validate(metadata))

    //Validate TaxonVerionKey exits
    val v1 = new Nbnv56Validator
    results.append(v1.validate(metadata))

    //validate dates fields have been defined
    val v2 = new Nbnv57Validator
    results.append(v2.validate(metadata))

    //validate a location fields have been defined
    val v3 = new Nbnv58Validator
    results.append(v3.validate(metadata))

    results.toList
  }
}
