package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.apache.log4j.Logger
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.metadata.MetadataReader
import javax.persistence.{EntityManager, EntityTransaction}
import uk.org.nbn.nbnv.importer.data.{DatasetIngester, RecordIngester}
import org.gbif.dwc.text.{StarRecord, Archive}
import java.util.Iterator
import org.gbif.utils.file.ClosableIterator

@RunWith(classOf[JUnitRunner])
class ImporterSuite extends FunSuite with ShouldMatchers {

  test("importer should say hello") {
    val log = mock(classOf[Logger])
    val archive = mock(classOf[Archive])
    val iterator = mock(classOf[ClosableIterator[StarRecord]])
    when(archive.iteratorRaw).thenReturn(iterator)
    
    val archiveManager = mock(classOf[ArchiveManager])
    when(archiveManager.open).thenReturn(archive)
    
    val metadataReader = mock(classOf[MetadataReader])

    val tx = mock(classOf[EntityTransaction])
    val entityManager = mock(classOf[EntityManager])
    when(entityManager.getTransaction).thenReturn(tx)

    val datasetIngester = mock(classOf[DatasetIngester])
    val recordIngester = mock(classOf[RecordIngester])
    

    val importer = new Importer(Options(), log, archiveManager, metadataReader
                                , entityManager,datasetIngester,recordIngester)
    importer.run()

    verify(log).info(startsWith("Welcome"))
  }
}
