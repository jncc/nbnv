package uk.org.nbn.nbnv.importer.grin

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

/// The command line options that can be provided to the importer.
case class Options(dataPath: String  = "archive.zip",
                   logDir:      String  = ".",
                   whatIf:      Boolean = false)
{
  override def toString = {
    Map(
      "  dataPath: %s" -> dataPath,
      "  logDir:      %s" -> logDir,
      "  whatIf:      %s" -> whatIf)
      .map(x => x._1.format(x._2))
      .mkString("\n")
  }
}

object Options {

  val usage =
    """|
      |Usage:
      |importer "C:\some\archive.zip" (specify a newline-delimited file of grid references)
      |         [-logDir "C:\logs"]   (specify a log directory)
      |         [-whatIf]             (don't commit)
    """.stripMargin

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    def process(options: Options, in: List[String]) : Options = in match {
      case Nil                     => options
      case "-logdir"  :: v :: tail => process(options.copy(logDir = v), tail)
      case "-whatif"       :: tail => process(options.copy(whatIf = true), tail)
      case v               :: tail => process(options.copy(dataPath = v), tail)
    }

    args match {
      case Nil => OptionsFailure(usage)
      case _   => OptionsSuccess(process(Options(), args.map(_.toLowerCase)))
    }
  }
}
