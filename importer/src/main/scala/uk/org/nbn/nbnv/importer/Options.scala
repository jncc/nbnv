package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult

object Options {
  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {
    OptionsFailure("Thanks for using the importer!")
  }
}

/// The command line options that can be provided to the importer.
abstract class Options {
  val archivePath:  String
  val tempDirectory: String
  val logDirectory: String
  val validateOnly: Boolean
}
