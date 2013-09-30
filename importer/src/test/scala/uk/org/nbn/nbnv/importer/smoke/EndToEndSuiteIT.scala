package uk.org.nbn.nbnv.importer.smoke

import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer._
import java.net.{URI, URL}


import metadata.{MetadataParser, MetadataReader}
import org.gbif.dwc.text.ArchiveFactory
import scala.collection.JavaConversions._
import com.google.common.base.Stopwatch
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.validation.Validator
import org.apache.log4j.Logger
import data.{Repository, QueryCache, Database}
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.utility.FileSystem


class EndToEndSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture(archiveURL: URL) = new {

    val archive = archiveURL
    val options = Options(archivePath = archive.getFile, target = Target.ingest)

    val importer = Importer.createImporter(options)
  }

  // change from 'ignore' to 'test' to run the importer against an archive within your IDE
  ignore("import an archive") {

    val archive = new URL("file:///C://Working//NBN test dataset//testarchive.zip")
    val f = fixture(archive)
    f.importer.run()
  }

  ignore("Make reader sloooooowww") {
    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_10000R.zip")

    val watch = new Stopwatch()

    watch.start()
    val temp = new File("./temp")
    temp.mkdirs()

    val archive = ArchiveFactory.openArchive(new File(archiveURL.getFile), temp)

    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      //get the first extension this will fail
//      Console.println("Record index %d".format(i))
      var nbnRecord = new NbnRecord(record);
//      Console.println("key: %s".format(nbnRecord.key))
    }

    Console.println("time %d".format(watch.elapsedMillis()))
  }

  ignore("Validation speed")  {

    val log = mock[Logger]


    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val qc = new QueryCache(log)
    val db = new Database(em, new Repository(log, em, qc), qc)

    val v = new Validator(log,db)

//    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_10000R.zip")
    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_19062013_121028.zip")

    val watch = new Stopwatch()

    watch.start()
    val temp = new File("./temp")
    temp.mkdirs()

    val archive = ArchiveFactory.openArchive(new File(archiveURL.getFile), temp)
    val metadataReader = new MetadataReader (new FileSystem, new  MetadataParser)
    val metadata = metadataReader.read(archive)

    v.validate(archive, metadata)

    Console.println("time %d".format(watch.elapsedMillis()))

  }

  test("should import a valid archive") {

    val archive = resource("/archives/valid.zip")


    val f = fixture(archive)
    f.importer.run()
  }

  ignore("should throw on non-existent dataset key") {

    val ex = intercept[BadDataException] {

      val archive = resource("/archives/nonexistent_dataset_key.zip")
      val f = fixture(archive)
      f.importer.run()
    }

    ex.message should include ("Dataset 'DOESNOTEXIST' does not exist")
  }


}
