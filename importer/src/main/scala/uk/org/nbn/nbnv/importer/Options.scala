package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

/// Represents a failure to parse the command line arguments.
// case class OptionsException(message: String) extends Exception(message)

/// The command line options that can be provided to the importer.
case class Options(archivePath: String  = "archive.zip",
                   tempDir:     String  = ".",
                   logDir:      String  = "./temp",
                   logLevel:    String  = "INFO",
                   whatIf:      Boolean = false)
{
  override def toString() = {
    Map(
      "  archivePath: %s" -> archivePath,
      "  tempDir:     %s" -> tempDir,
      "  logDir:      %s" -> logDir,
      "  logLevel:    %s" -> logLevel,
      "  whatIf:      %s" -> whatIf)
      .map(x => x._1.format(x._2))
      .mkString("\n")
  }
}

object Options {

  val usage =
    """|
       |Usage:
       |importer "C:\some\archive.zip" (specify a DwcA archive)
       |         [-logDir "C:\logs"]   (specify a log directory)
       |         [-logLevel DEBUG]     (specify a log level)
       |         [-tempDir "C:\temp"]  (specify a temp directory)
       |         [-whatIf]             (validate only, don't import)
    """.stripMargin

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    def process(options: Options, in: List[String]) : Options = in match {
      case Nil                      => options
      case "-logdir"   :: v :: tail => process(options.copy(logDir = v), tail)
      case "-loglevel" :: v :: tail => process(options.copy(logDir = v), tail)
      case "-tempdir"  :: v :: tail => process(options.copy(tempDir = v), tail)
      case "-whatif"        :: tail => process(options.copy(whatIf = true), tail)
      case v                :: tail => process(options.copy(archivePath = v), tail)
    }

    args match {
      case Nil => OptionsFailure(usage)
      case _   => OptionsSuccess(process(Options(), args.map(_.toLowerCase)))
    }
  }
}
