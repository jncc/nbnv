package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import ingestion.Ingester
import org.apache.log4j.Logger
import org.mockito.Mockito._
import org.mockito.Matchers._
import testing.BaseFunSuite
import uk.org.nbn.nbnv.metadata.MetadataReader
import javax.persistence.{EntityManager, EntityTransaction}
import org.gbif.dwc.text.Archive

class ImporterSuite extends BaseFunSuite  {

  test("importer should say hello") {

    // arrange

    val log = mock[Logger]
    val archive = mock[Archive]

    val archiveManager = mock[ArchiveManager]
    when(archiveManager.open()).thenReturn(archive)
    
    val metadataReader = mock[MetadataReader]

    val entityManager = mock[EntityManager]
    when(entityManager.getTransaction).thenReturn(mock[EntityTransaction])

    val ingester = mock[Ingester]

    // act
    val importer = new Importer(Options(), log, archiveManager, metadataReader, ingester)
    importer.run()

    // assert
    verify(log).info(startsWith("Welcome"))
  }
}
