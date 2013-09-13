package uk.org.nbn.nbnv.importer.validation

import scala.collection.JavaConversions._
import com.google.inject.Inject
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.{Database}
import uk.org.nbn.nbnv.importer.metadata.Metadata

// todo: mapping between darwin and nbn terms, separate from reading values, nulls throw?
// todo: ensure possibility for parallel


class Validator @Inject()(log: Logger, db: Database ){

  private var errors = 0 // count the validation errors

  def validate(archive: Archive, metadata: Metadata) {

    log.info("Validating metadata...")

    val metaValidator = new MetadataValidator(db.repo)
    val mResults = metaValidator.validate(metadata)
    for (result <- mResults) processResult(result)

    if (errors > 0) {
      throw new BadDataException("Failed due to bad metadata")
    }

    log.info("Validating archive...")

    //    (1) head scoped / required - can't validate darwin mappings for validation.
    //      ask gbif to alter reader to isMapped or list of defined mappings. at the mo we're checking for null in first record. perhaps null means not mapped
    val validator = new ArchiveHeadValidator
    val results = validator.validate(archive)
    for (result <- results) processResult(result)

    if (errors > 0) {
      throw new BadDataException("Failed due to invalid data file structure")
    }


    // (2) archive scoped / aggregate value validation (e.g. no duplicate record keys)
    // This currently won't work because of case
//    val aggregateValidators = List(new Nbnv61Validator)



    val duplicateValidator = new Nbnv61Validator

    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val result2 = duplicateValidator.processRecord(record)
      processResult(result2)
    }

    if (errors > 0) {
      throw new BadDataException("Failed due to duplicated records")
    }

    // (3) record-scoped
    // parsing/conversions - don't want to duplicate the parsing logic
    // size (length)
    // lookups (range) (e.g. checking real taxon key)


    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {

      if (i % 100 == 99) {
        log.info("Validated %d records".format(i))
      }

      val nbnRecord = new NbnRecord(record)

      //validate SurveyKey length
      val v0 = new Nbnv62Validator
      val r0 = v0.validate(nbnRecord)
      processResult(r0)

      //validate SampleKey length
      val v1 = new Nbnv63Validator
      val r1 = v1.validate(nbnRecord)
      processResult(r1)

      //validate taxon version key exists
      val v2 = new Nbnv64Validator(db.repo)
      val r2 = v2.validate(nbnRecord)
      processResult(r2)

      //validate Absence flag
      val v3 = new Nbnv66Validator
      val r3 = v3.validate(nbnRecord)
      processResult(r3)

      //validate sensitive flag
      val v4 = new Nbnv67Validator
      val r4 = v4.validate(nbnRecord)
      processResult(r4)

      //validate SiteKey length
      val v5 = new Nbnv79Validator
      val r5 = v5.validate(nbnRecord)
      processResult(r5)

      //validate SiteName field
      val v6 = new Nbnv80Validator
      val r6 = v6.validate(nbnRecord)
      processResult(r6)

      //validate Recorder field length
      val v7 = new Nbnv91Validator
      val r7 = v7.validate(nbnRecord)
      processResult(r7)

      //validate Determiner field length
      val v8 = new Nbnv92Validator
      val r8 = v8.validate(nbnRecord)
      processResult(r8)

      //validate RecordKey is provided
      val v9 = new Nbnv163Validator
      val r9 = v9.validate(nbnRecord)
      processResult(r9)

      // Validates a set of dates
      val dv = new DateValidator
      val dvResults = dv.validate(nbnRecord)
      for (result <- dvResults) processResult(result)

      val srv = new SpatialReferenceValidator(db)
      val srvResults = srv.validate(nbnRecord)
      for (result <- srvResults) processResult(result)

      //Validates each attribute and returns a set of results
      val oav = new ObservationAttributeValidator
      val oavResults = oav.validate(nbnRecord)
      for (result <- oavResults) processResult(result)

//      // call aggregation callbacks
//      for (v <- aggregateValidators) {
//        val result = v.processRecord(nbnRecord)
//        processResult(result)
//      }

    }

//    for (v <- aggregateValidators) {
//      v.notifyComplete()
//    }

    log.info("Validation complete. %d validation errors".format(errors))

    if (errors > 0) {
      throw new BadDataException("Failed due to validation errors")
    }
  }

  private def processResult(result: Result) {

    val output = "Validation [%s] %s".format(result.reference, result.message)

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



