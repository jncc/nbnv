package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat

class NbnDateParserSuite extends BaseFunSuite {

  test("Should return start date as end date when no end date specified and date type is D") {
    val dateParser = new NbnDateParser()

    val (startDate, endDate) = dateParser.parse("D", Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")), None)

    startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")))
    endDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")))
  }

}
