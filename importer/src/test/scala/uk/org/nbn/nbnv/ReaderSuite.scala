
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import uk.org.nbn.nbnv.Reader

class ReaderSuite extends FunSuite with ShouldMatchers {
  test("should pass") {
    val reader = new Reader()
    reader.read()

    true should be (true)
  }
}
