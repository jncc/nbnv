package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

/// Represents a failure to parse the command line arguments.
// case class OptionsException(message: String) extends Exception(message)

/// The command line options that can be provided to the importer.
case class Options(archivePath: String  = "specify-an-archive-path.zip",
                   target:      Target.Value = Target.commit,
                   logLevel:    String  = "INFO",
                   logDir:      String  = ".",
                   tempDir:     String  = "./temp")
{
  override def toString = {
    Map(
      "  archivePath %s" -> archivePath,
      "  target      %s" -> target,
      "  logLevel    %s" -> logLevel,
      "  logDir      %s" -> logDir,
      "  tempDir     %s" -> tempDir)
      .map(x => x._1.format(x._2))
      .mkString("\n")
  }
}

object Options {

  val usage =
    """|
       |Usage:
       |importer "C:\some\archive.zip" (specify a DwcA archive)
       |         [-target validate]    (one of validate|verify|ingest|commit)
       |         [-logLevel DEBUG]     (specify a log level)
       |         [-logDir "C:\logs"]   (specify a log directory)
       |         [-tempDir "C:\temp"]  (specify a temp directory)
    """.stripMargin

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    def process(options: Options, in: List[String]) : Options = in match {
      case Nil                      => options
      case "-tempdir"  :: v :: tail => process(options.copy(tempDir = v), tail)
      case "-logdir"   :: v :: tail => process(options.copy(logDir = v), tail)
      case "-loglevel" :: v :: tail => process(options.copy(logLevel = v), tail)
      case "-target"   :: v :: tail => process(options.copy(target = Target.withName(v.toLowerCase)), tail)
      case v                :: tail => process(options.copy(archivePath = v), tail)
    }

    args match {
      case Nil => OptionsFailure(usage)
      case _   => OptionsSuccess(process(Options(), args.map(_.toLowerCase)))
    }
  }
}

/// The possible "targets", or steps, of the importer.
/// Note: the order of these values is significant.
object Target extends Enumeration {
  val validate = Value("validate")
  val verify   = Value("verify")
  val ingest   = Value("ingest")
  val commit   = Value("commit")
}

