package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

/// The command line options that can be provided to the importer.
abstract class Options {
  val archivePath:  String
  val tempDir: String
  val logDir: String
  val whatIf: Boolean
}

object Options {

  type OptionMap = Map[Symbol, Any]

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    // todo catch exception??
    val usage =
      """|importer "C:\some\archive.zip" (specify a DwcA archive)
         |         [-logDir "C:\logs"]   (specify a log directory)
         |         [-tempDir "C:\temp"]  (specify a temp directory)
         |         [-whatIf]             (validate only, don't import)
      """.stripMargin

    def process(out: OptionMap, in: List[String]): OptionMap = in match {
        case Nil => out
        case "-tempdir" :: v :: tail => process(out ++ Map('tempDir -> v), tail)
        case "-logdir"  :: v :: tail => process(out ++ Map('logDir  -> v), tail)
        case "-whatif"  :: v :: tail => process(out ++ Map('whatIf  -> v.toBoolean), tail)
        case arg :: tail             => process(out ++ Map('arg -> arg), tail)
    }

    args match {
      case Nil => OptionsFailure(usage)
      case _ => {
        // include defaults
        val options = process(Map(), args.map(_.toLowerCase))
        val o = new Options {
          val archivePath = options.get('archivePath) match {
            case Some(s: String) => s
            case None => "" // todo throw new Exception? :-(
          }
          val tempDir = "."
          val logDir = "."
          val whatIf = options.get('whatIf) match {
            case Some(b: Boolean) => b
            case None             => false
          }
        }
        OptionsSuccess(o)
      }
    }
  }
}


