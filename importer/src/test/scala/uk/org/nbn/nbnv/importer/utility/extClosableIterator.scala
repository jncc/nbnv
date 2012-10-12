package uk.org.nbn.nbnv.importer.utility

import java.util
import org.gbif.utils.file.ClosableIterator

/**
 * Created with IntelliJ IDEA.
 * User: Matt Debont
 * Date: 12/10/12
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
class extClosableIterator[StarRecord](list : util.ArrayList[StarRecord]) extends ClosableIterator[StarRecord] {
  val it : java.util.Iterator[StarRecord] = (list.iterator())

  override def remove { it.remove }
  override def hasNext: Boolean = { it.hasNext }
  override def next: StarRecord = { it.next }
  override def close { }
}
