package uk.org.nbn.nbnv.importer.validation

import scala.collection.JavaConversions._
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.archive.Archive

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

    val validator = new ArchiveHeadValidator
    val results = validator.validate(archive)
    for (result <- results) processResult(result)

    if (errors > 0) {
      throw new BadDataException("Failed due to invalid data file structure, see log for details")
    }

    val duplicateValidator = new Nbnv61Validator

    for ((nbnRecord, i) <- archive.records.zipWithIndex) {

      if (i % 100 == 99) {
        log.info("Validated %d records".format(i+1))
      }

      val dupeResult = duplicateValidator.processRecord(nbnRecord)
      processResult(dupeResult)

      //validate RecordKey is provided
      val v9 = new Nbnv163Validator
      val r9results = v9.validate(nbnRecord, i)
      for (result <- r9results) processResult(result)

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
    }

    log.info("Validation complete. %d validation errors".format(errors))

    if (errors > 0) {
      throw new BadDataException("Failed due to validation errors, see log for details")
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



