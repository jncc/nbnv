package uk.org.nbn.nbnv.importer.grin

import scala.collection.JavaConversions._
import com.google.inject.{Inject, Guice}
import injection.GuiceModule
import uk.org.nbn.nbnv.importer.ingestion.FeatureIngester
import javax.persistence.{EntityTransaction, EntityManager}
import io.Source
import org.apache.log4j.Logger
import javax.validation.ConstraintViolationException
import com.google.common.base.Stopwatch
import uk.org.nbn.nbnv.importer.data.{Database, QueryCache, CoreRepository}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.records.GridRefDef

object Program {

  /// The entry point to the console application.
  def main(args: Array[String]) {

    Options.parse(args.toList) match {
      case OptionsSuccess(options) => {
        val program = create(options)
        program.run()
      }
      case OptionsFailure(message) => {
        println(message)
        sys.exit(1)
      }
    }
  }

  def create(options: Options) = {

    val injector = Guice.createInjector(new GuiceModule(options))
    injector.getInstance(classOf[Program])
  }
}

class Program @Inject() (log: Logger, options: Options, db: Database, ingester: FeatureIngester) {

  def run() {

    val watch = new Stopwatch().start()

    val groups = (for {
      line <- Source.fromFile(options.dataPath).getLines()
      ref = line.trim
      if !ref.isEmpty
    } yield ref).zipWithIndex.grouped(100)

    for (g <- groups) {

      val t = db.em.getTransaction

      val ingester = new FeatureIngester(log, db, new GridSquareInfoFactory(db))

      withTransaction(t, options.whatIf) {

        for ((ref, i) <- g) {
          log.info("Importing grid ref '%s'".format(ref))
          ingester.ensureGridRefFeature(GridRefDef(ref, None, None))
          log.info("Imported " + (i + 1) + " grid refs in " + watch.elapsedMillis() + "ms")
          log.info("Average speed is " + watch.elapsedMillis() / (i + 1) + "ms per grid ref")
        }
        db.flushAndClear()
      }
    }
  }

  def withTransaction(t: EntityTransaction, whatIf: Boolean)(f: => Unit) {

    t.begin()

    try {
      f

      if (whatIf) {
        log.info("Rolling back transaction (whatIf=true)")
        t.rollback()
      }
      else {
        log.info("Committing transaction")
        t.commit()
      }
    }
    catch {
      case e: ConstraintViolationException => {
        log.fatal("Unhandled exception!", e)
        for (v <- e.getConstraintViolations) {
          log.info(v.getPropertyPath + v.getMessage)
        }
        throw e
      }
      case e: Throwable => {
        if (t != null && t.isActive) t.rollback()
        log.fatal("Unhandled exception!", e)
        throw (e)
      }
    }
    finally {
      //em.close()
    }
  }

}

//truncate table gridsquare
//truncate table featurecontains
//truncate table FeatureOverlaps
//truncate table GridTree
//delete from feature
//DBCC CHECKIDENT('Feature', RESEED, 0)
