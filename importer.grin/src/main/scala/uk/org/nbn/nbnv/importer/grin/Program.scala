package uk.org.nbn.nbnv.importer.grin

import com.google.inject.Guice
import injection.GuiceModule
import uk.org.nbn.nbnv.importer.ingestion.FeatureIngester
import javax.persistence.{EntityTransaction, EntityManager}
import io.Source
import org.apache.log4j.Logger

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

class Program (log: Logger, options: Options, em: EntityManager, ingester: FeatureIngester) {

  def run() {


    val t = em.getTransaction

    withEntityTransaction(t) {

      t.begin()

      for (line <- Source.fromFile(options.dataPath).getLines.take(10)) {
        val gridRef = line.trim
        ingester.ensureGridRefFeature(gridRef)
      }

      if (options.whatIf) {
        log.info("Rolling back transaction (whatIf=true)")
        t.rollback()
      }
      else {
        log.info("Committing transaction")
        t.commit()
      }
    }
  }

  def withEntityTransaction(t: EntityTransaction)(f: => Unit) {
    try {
      f
    }
    catch {
      case e: Throwable => {
        if (t != null && t.isActive) t.rollback()
        throw (e)
      }
    }
    finally {
      em.close()
    }
  }
}

