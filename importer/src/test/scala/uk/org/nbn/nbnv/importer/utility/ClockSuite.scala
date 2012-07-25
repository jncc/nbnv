package uk.org.nbn.nbnv.importer.utility

import java.util.{Date, Calendar}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import utility.Clock
import uk.org.nbn.nbnv.importer.Clock
import uk.org.nbn.nbnv.Clock

@RunWith(classOf[JUnitRunner])
class ClockSuite extends FunSuite with ShouldMatchers {

  /*test("clock tells the time") {
   val before = Calendar.getInstance().getTime
   val d = Clock.nowUtc()
   val after = Calendar.getInstance().getTime
   before.before(d) should be (true)
   after.after(d) should be (true)
   }

   test("clock can be set") {
   val birthday = new Date(2012, 2, 17, 5, 30, 0)
   Clock.f = () => birthday
   val d = Clock.nowUtc()
   d should be (birthday)
   }

   test("clock can be reset") {
   val birthday = new Date(2012, 2, 17, 5, 30, 0)
   Clock.f = () => birthday
   Clock.reset()
   val d = Clock.nowUtc()
   d should not be (birthday)

  }*/
}
