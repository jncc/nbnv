package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

/// Represents a failure to parse the command line arguments.
// case class OptionsException(message: String) extends Exception(message)

/// The command line options that can be provided to the importer.
abstract class Options {
  val archivePath:  String
  val tempDir: String
  val logDir: String
  val whatIf: Boolean
}

object Options {

  type OptionMap = Map[Symbol, Any]

  val usage =
    """|
       |Usage:
       |importer "C:\some\archive.zip" (specify a DwcA archive)
       |         [-logDir "C:\logs"]   (specify a log directory)
       |         [-tempDir "C:\temp"]  (specify a temp directory)
       |         [-whatIf]             (validate only, don't import)
    """.stripMargin

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    def process(out: OptionMap, in: List[String]): OptionMap = in match {
      case Nil                     => out
      case "-logdir"  :: v :: tail => process(out ++ Map('logDir -> v), tail)
      case "-tempdir" :: v :: tail => process(out ++ Map('tempDir -> v), tail)
      case "-whatif"       :: tail => process(out ++ Map('whatIf -> true), tail)
      case v               :: tail => process(out ++ Map('archivePath -> v), tail)
    }

    args match {
      case Nil => OptionsFailure(usage)
      case _   => {
        val options = process(Map(), args.map(_.toLowerCase))
        createSuccessObject(options)
      }
    }
  }

  def createSuccessObject(map: OptionMap) : OptionsResult = {

    def getString(options: OptionMap, name: Symbol): String = options.get(name) match {
      case Some(s: String) => s
      case None => "" // todo throw new Exception? :-(
    }

    val options = new Options {
      val archivePath = getString(map, 'archivePath)
      val logDir      = getString(map, 'logDir)
      val tempDir     = getString(map, 'tempDir)
      val whatIf      = map.get('whatIf) match {
        case Some(b: Boolean) => b
        case None             => false
      }
    }
    OptionsSuccess(options)
  }
}
