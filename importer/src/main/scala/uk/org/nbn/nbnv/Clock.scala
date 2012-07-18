package uk.org.nbn.nbnv

import java.util.{Date, Calendar}

object Clock {
  // by default, use the real system time
  var f: () => Date = Calendar.getInstance().getTime
  def nowUtc() = f()
  def reset() = f = Calendar.getInstance().getTime
}
