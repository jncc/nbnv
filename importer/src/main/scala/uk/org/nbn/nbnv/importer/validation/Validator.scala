package uk.org.nbn.nbnv.importer.validation

import scala.collection.JavaConversions._
import com.google.inject.Inject
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.Repository

// todo: requirement for allowing e.g. 100 errors - presumably this needs to keep validating, but not import?
// todo: mapping between darwin and nbn terms, separate from reading values, nulls throw?
// todo: ensure possibility for parallel


class Validator @Inject()(log: Logger, repo: Repository){

  def validate(archive: Archive) {
    log.info("Hello from the validator.")
//    (1) head scoped / required - can't validate darwin mappings for validation.
//      ask gbif to alter reader to isMapped or list of defined mappings. at the mo we're checking for null in first record. perhaps null means not mapped

    var validator = new ArchiveHeadValidator
    var results = validator.validate(archive)
    for (result <- results) logResult(result)


//    (2) archive scoped / aggregate value validation (e.g. no duplicate record keys)
//
//    (3) record-scoped
//    parsing/conversions - don't want to duplicate the parsing logic
//    size (length)
//    lookups (range) (e.g. checking real taxon key)


    for (record <- archive.iteratorRaw) {

      var nbnRecord = new NbnRecord(record)

      val v0 = new Nbnv62Validator
      val r0 = v0.validate(nbnRecord)
      logResult(r0)

      val v1 = new Nbnv63Validator
      val r1 = v1.validate(nbnRecord)
      logResult(r1)

      val v2 = new Nbnv64Validator(repo)
      val r2 = v2.validate(nbnRecord)
      logResult(r2)

      val v3 = new Nbnv66Validator
      val r3 = v3.validate(nbnRecord)
      logResult(r3)

      val v4 = new Nbnv67Validator
      val r4 = v4.validate(nbnRecord)
      logResult(r4)

      val v5 = new Nbnv79Validator
      val r5 = v5.validate(nbnRecord)
      logResult(r5)

      val v6 = new Nbnv80Validator
      val r6 = v6.validate(nbnRecord)
      logResult(r6)

      val v7 = new Nbnv91Validator
      val r7 = v7.validate(nbnRecord)
      logResult(r7)

      val v8 = new Nbnv92Validator
      val r8 = v8.validate(nbnRecord)
      logResult(r8)

      //Validates each attribute and returns a set of results
      val oav = new ObservationAttributeValidator
      val oavResults = oav.validate(nbnRecord)
      for (result <- oavResults) logResult(result)
    }
  }

  private def logResult(result: Result) {

    val output = "Validation: " + result.reference + " | " + result.message

    result.level match {
      case ResultLevel.DEBUG => log.debug(output)
      case ResultLevel.INFO  => log.info(output)
      case ResultLevel.WARN  => log.warn(output)
      case ResultLevel.ERROR => {
        log.error(output)
        throw new ImportFailedException("Validation failure.")
      }
    }
  }
}
