package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.data.{CoreRepository, Database}
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.scalatest.BeforeAndAfter

class Nbnv88ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _
  var db : Database = _
  var repo : CoreRepository = _

  before {
    record = mock[NbnRecord]
    repo = mock[CoreRepository]
    db = mock[Database]

    when(db.coreRepo).thenReturn(repo)
  }


  test("should validate a valid FeatureKey") {
    when(record.featureKey).thenReturn(Option("DS123456PK123"))
    when(repo.confirmSiteBoundary("DS123456","PK123")).thenReturn(true)

    val v = new Nbnv88Validator(db)
    var r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid FeatureKey") {
    when(record.featureKey).thenReturn(Option("DS123456PK123"))
    when(repo.confirmSiteBoundary("DS123456","PK123")).thenReturn(false)

    val v = new Nbnv88Validator(db)
    var r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a FeatureKey that is less then 9 charachters") {
    when(record.featureKey).thenReturn(Option("1"))

    val v = new Nbnv88Validator(db)
    var r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

}
