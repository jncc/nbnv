package uk.org.nbn.nbnv.importer.validation

import scala.collection.JavaConversions._
import com.google.inject.Inject
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.{Database}

// todo: mapping between darwin and nbn terms, separate from reading values, nulls throw?
// todo: ensure possibility for parallel


class Validator @Inject()(log: Logger, db: Database){

  private var errors = 0 // count the validation errors

  def validate(archive: Archive) {

    log.info("Validating archive...")

    //    (1) head scoped / required - can't validate darwin mappings for validation.
    //      ask gbif to alter reader to isMapped or list of defined mappings. at the mo we're checking for null in first record. perhaps null means not mapped

    val validator = new ArchiveHeadValidator
    val results = validator.validate(archive)
    for (result <- results) processResult(result, "NBNV-HEAD")


    // (2) archive scoped / aggregate value validation (e.g. no duplicate record keys)
    //
    val aggregateValidators = List(new Nbnv61Validator)

    // (3) record-scoped
    // parsing/conversions - don't want to duplicate the parsing logic
    // size (length)
    // lookups (range) (e.g. checking real taxon key)


    for (record <- archive.iteratorRaw) {

      val nbnRecord = new NbnRecord(record)

      val v0 = new Nbnv62Validator
      val r0 = v0.validate(nbnRecord)
      processResult(r0, "NBNV-62")

      val v1 = new Nbnv63Validator
      val r1 = v1.validate(nbnRecord)
      processResult(r1, "NBNV-63")

      val v2 = new Nbnv64Validator(db.repo)
      val r2 = v2.validate(nbnRecord)
      processResult(r2, "NBNV-64")

      val v3 = new Nbnv66Validator
      val r3 = v3.validate(nbnRecord)
      processResult(r3, "NBNV-66")

      val v4 = new Nbnv67Validator
      val r4 = v4.validate(nbnRecord)
      processResult(r4, "NBNV-67")

      val v5 = new Nbnv79Validator
      val r5 = v5.validate(nbnRecord)
      processResult(r5, "NBNV-79")

      val v6 = new Nbnv80Validator
      val r6 = v6.validate(nbnRecord)
      processResult(r6, "NBNV-80")

      val v7 = new Nbnv91Validator
      val r7 = v7.validate(nbnRecord)
      processResult(r7, "NBNV-91")

      val v8 = new Nbnv92Validator
      val r8 = v8.validate(nbnRecord)
      processResult(r8, "NBNV-92")

      val v9 = new Nbnv163Validator
      val r9 = v9.validate(nbnRecord)
      processResult(r9, "NBNV-163")

      // Validates a set of dates
      val dv = new DateValidator
      val dvResults = dv.validate(nbnRecord)
      for (result <- dvResults) processResult(result, "NBNV-DATE")

      val srv = new SpatialReferenceValidator(db)
      val srvResults = srv.validate(nbnRecord)
      for (result <- srvResults) processResult(result, "NBNV-SPATIAL-REFERENCE")

      //Validates each attribute and returns a set of results
      val oav = new ObservationAttributeValidator
      val oavResults = oav.validate(nbnRecord)
      for (result <- oavResults) processResult(result, "NBNV-ATTRIBUTE")

      // call aggregation callbacks
      for (v <- aggregateValidators) {
        val result = v.processRecord(nbnRecord)
        processResult(result, v.name)
      }

    }

    for (v <- aggregateValidators) {
      v.notifyComplete()
    }

    log.info("Validation complete. %d validation errors".format(errors))

    if (errors > 0) {
      throw new ImportFailedException("Failed due to validation errors")
    }
  }

  private def processResult(result: Result, code: String) {

    val output = "Validation: " + result.reference + " | " + result.message

    result.level match {
      case ResultLevel.DEBUG => log.debug(output)
      case ResultLevel.INFO  => log.info(output)
      case ResultLevel.WARN  => log.warn(output)
      case ResultLevel.ERROR => {
        log.error(output)
        errors = errors + 1
      }
    }
  }
}



