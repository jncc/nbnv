package uk.org.nbn.nbnv.importer.smoke

import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer._
import archive._
import java.net.URL
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.utility.FileSystem
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.BadDataException

class EndToEndSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture(archiveURL: URL, target: Target.Value = Target.ingest) = new {

    val archive = archiveURL
    val options = Options(archivePath = archive.getFile, target = target)

    val importer = Importer.createImporter(options)
  }

  // change from 'ignore' to 'test' to run the importer against an archive within your IDE
  ignore("import an archive") {
    val archive = new URL("file:///C://Working//nbnv-877//archive_CBDC_17122013_103319_Private.zip")
    val f = fixture(archive, Target.ingest)
    f.importer.run()
  }

  ignore("data file parser") {
    val pathUrl = new URL("file:///C://Working//testreader//archive_CBDC_17122013_103319_Private//data.tab2")
    val mappingUrl = new URL("file:///C://Working//testreader//archive_CBDC_17122013_103319_Private//meta.xml")

    val fs = new FileSystem
    val xml = fs.loadXml(mappingUrl.getFile)
    val parser = new ArchiveMetadataParser

    val metadata = parser.getMetadata(xml)

    val options = mock[Options]
    val log = mock[Logger]
    val crf = new CsvReaderFactory
    val rf = new NbnRecordFactory(log)
    val dfp = new DataFileParser(options, rf, log, crf)

    dfp.open(pathUrl.getFile, metadata)

    var index = 0



    dfp.records.foreach(r => {
      if (index == 0 || index % 100 == 0) println("%s, line %d".format(r.key, index))
      index = index+1
    })
  }


  ignore("csv reader") {
//    val pathUrl = resource("/archives/valid/data.tab")
    val pathUrl = new URL("file:///C://Working//testreader//archive_CBDC_17122013_103319_Private//data.tab2")
    val mappingUrl = new URL("file:///C://Working//testreader//archive_CBDC_17122013_103319_Private//meta.xml")

    val fs = new FileSystem
    val xml = fs.loadXml(mappingUrl.getFile)
    val parser = new ArchiveMetadataParser

    val metadata = parser.getMetadata(xml)


    val csvReader = new CsvReader(new File(pathUrl.getFile))

    val log = mock[Logger]
    val recordFac = new NbnRecordFactory(log)
    var index = 0
//    csvReader.drop(1).foreach(x => {
//      val record = recordFac.makeRecord(x, metadata)
//      if (index % 100 == 0) println("%s [%d] : length: %d".format(record.key, index, x.length))
//      index = index+1
//    })

    val x = csvReader.drop(metadata.skipHeaderLines.getOrElse(0)).view.zipWithIndex.map({ case (rawData, i) =>
      //      if (rawData.length < metadata.fields) {
      //        log.warn("The record at row %d contains less fields then the %d mapped fields".format(i + 1,metadata.fields))
      //      }

      recordFac.makeRecord(rawData, metadata)
    })



    csvReader.drop(1).zipWithIndex.map{case (record, index) =>
      val nbnrec = recordFac.makeRecord(record, metadata)
      if (index == 0 || index % 100 == 0) println("%s [%d] : length: %d".format(nbnrec.key, index, record.length))
    }
  }
//  ignore("Make reader sloooooowww") {
//    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_10000R.zip")
//
//    val watch = new Stopwatch()
//
//    watch.start()
//    val temp = new File("./temp")
//    temp.mkdirs()
//
//    val archive = ArchiveFactory.openArchive(new File(archiveURL.getFile), temp)
//
//    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
//      //get the first extension this will fail
////      Console.println("Record index %d".format(i))
//      var nbnRecord = new NbnRecord(record);
////      Console.println("key: %s".format(nbnRecord.key))
//    }
//
//    Console.println("time %d".format(watch.elapsedMillis()))
//  }

//  ignore("Validation speed")  {
//
//    val log = mock[Logger]
//
//
//    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
//    val qc = new QueryCache(log)
//    val db = new Database(em, new Repository(log, em, qc), qc)
//
//    val v = new Validator(log,db)
//
////    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_10000R.zip")
//    val archiveURL = new URL("file:///C://Working//bwarstst//archive_BWARS_19062013_121028.zip")
//
//    val watch = new Stopwatch()
//
//    watch.start()
//    val temp = new File("./temp")
//    temp.mkdirs()
//
//    val archive = ArchiveFactory.openArchive(new File(archiveURL.getFile), temp)
//    val metadataReader = new MetadataReader (new FileSystem, new  MetadataParser)
//    val metadata = metadataReader.read(archive)
//
//    v.validate(archive, metadata)
//
//    Console.println("time %d".format(watch.elapsedMillis()))
//
//  }

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
