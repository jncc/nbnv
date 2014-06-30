package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

import scala.collection.mutable.ListBuffer

/**
 * Created by felix mason on 30/06/2014.
 */
class SiteValidator(duplicateSiteKeyValidator: DuplicateSiteKeyValidator) {
  def validate(record: NbnRecord) : List[Result] = {
    val resultList = new ListBuffer[Result]

    //validate SiteKey length
    val v0 = new Nbnv79Validator
    val r0 = v0.validate(record)
    resultList.append(r0)

    //validate SiteName field
    val v1 = new Nbnv80Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    if (resultList.find(r => r.level == ResultLevel.ERROR) == None && record.siteKey.isDefined) {
      val r2 = duplicateSiteKeyValidator.processRecord(record)
      resultList.append(r2)
    }

    resultList.toList
  }

}
