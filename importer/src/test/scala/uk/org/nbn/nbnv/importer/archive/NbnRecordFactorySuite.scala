package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.apache.log4j.Logger

class NbnRecordFactorySuite extends BaseFunSuite with BeforeAndAfter {
  var factory : NbnRecordFactory = _

  before {
    val log = mock[Logger]

    factory = new NbnRecordFactory(log)
  }

//  test("should parse a valid record") {
//
//  }

}
