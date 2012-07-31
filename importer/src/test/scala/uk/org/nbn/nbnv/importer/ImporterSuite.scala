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
import data.{Ingester, DatasetIngester, RecordIngester}
import org.gbif.dwc.text.{StarRecord, Archive}
import java.util.Iterator
import org.gbif.utils.file.ClosableIterator
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class ImporterSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  test("importer should say hello") {

    // arrange

    val log = mock[Logger]
    val archive = mock[Archive]
//    val iterator = mock[ClosableIterator[StarRecord]]
//    when(archive.iteratorRaw).thenReturn(iterator)
    
    val archiveManager = mock[ArchiveManager]
    when(archiveManager.open).thenReturn(archive)
    
    val metadataReader = mock[MetadataReader]

    val entityManager = mock[EntityManager]
    when(entityManager.getTransaction).thenReturn(mock[EntityTransaction])

    val ingester = mock[Ingester]

    //act
    val importer = new Importer(Options(), log, archiveManager, metadataReader, ingester)
    importer.run()

    // assert
    verify(log).info(startsWith("Welcome"))
  }
}
