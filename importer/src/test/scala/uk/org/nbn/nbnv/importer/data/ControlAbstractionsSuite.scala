package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.testing.BaseFunSpec
import uk.org.nbn.nbnv.importer.ImportFailedException

class ControlAbstractionsSuite extends BaseFunSpec with ControlAbstractions {

  describe("calling code within an expectSingleResult block") {

    it("should return the result when there's exactly one result") {

      val result = expectSingleResult("some-id") {
        List("some item")
      }

      result should be ("some item")
    }

    it("should throw an exception when there are no results") {

      val ex = intercept[ImportFailedException] {

        expectSingleResult("some-id") {
          List() // an empty list
        }
      }

      ex.message should be ("Expected one result for 'some-id', but found none.")
    }

    it("should throw an exception when there are more than one results") {

      val ex = intercept[ImportFailedException] {

        expectSingleResult("some-id") {
          List("item 1", "item 2", "item 3")
        }
      }

      ex.message should be ("Expected one result for 'some-id', but found 3.")
    }
  }
}
