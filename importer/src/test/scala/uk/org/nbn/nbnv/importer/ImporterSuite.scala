package uk.org.nbn.nbnv.importer

import archive.Archive
import darwin.ArchiveManager
import ingestion.Ingester
import org.apache.log4j.Logger
import org.mockito.Mockito._
import org.mockito.Matchers._
import testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.metadata.MetadataReader
import javax.persistence.{EntityManager, EntityTransaction}
import utility.Stopwatch
import validation.Validator

class ImporterSuite extends BaseFunSuite  {

  test("importer should say hello") {

    // arrange

    val log = mock[Logger]
    val archive = mock[Archive]
    
    val metadataReader = mock[MetadataReader]

    val entityManager = mock[EntityManager]
    when(entityManager.getTransaction).thenReturn(mock[EntityTransaction])

    val ingester = mock[Ingester]
    val validator = mock[Validator]

    // act
    val importer = new Importer(Options(), log, new Stopwatch(), archive, metadataReader, validator, ingester)
    importer.run()

    // assert
    verify(log).info(startsWith("Welcome"))
  }
}
