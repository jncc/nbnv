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

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    // todo catch exception
    val usage =
      """|
         |Usage:
         |importer "C:\some\archive.zip" (specify a DwcA archive)
         |         [-logDir "C:\logs"]   (specify a log directory)
         |         [-tempDir "C:\temp"]  (specify a temp directory)
         |         [-whatIf]             (validate only, don't import)
      """.stripMargin

    def isSwitch(s: String) = s.startsWith("-")

    def process(out: OptionMap, in: List[String]): OptionMap = in match {
      case Nil                             => out
      case "-tempdir" :: v :: tail         => process(out ++ Map('tempDir -> v), tail)
      case "-logdir"  :: v :: tail         => process(out ++ Map('logDir  -> v), tail)
      case "-whatif"  :: tail              => process(out ++ Map('whatIf  -> true), tail)
//    case arg :: s :: tail if isSwitch(s) => process(out ++ Map('arg     -> arg), in.tail)
      case arg :: tail                     => process(out ++ Map('arg     -> arg), tail)
    }

    def getString(options: OptionMap, name: Symbol): String = options.get(name) match {
      case Some(s: String) => s
      case None => "" // todo throw new Exception? :-(
    }
    args match {
      case Nil => OptionsFailure(usage)
      case _   => {
        // process the arguments
        val options = process(Map(), args.map(_.toLowerCase))
        // include defaults
        val o = new Options {
          val archivePath = getString(options, 'arg)
          val logDir = getString(options, 'logDir)
          val tempDir = getString(options, 'tempDir)
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


