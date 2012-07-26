package uk.org.nbn.nbnv.importer

/// The result of trying to parse the command line options.
sealed abstract class OptionsResult
case class OptionsSuccess(options: Options) extends OptionsResult
case class OptionsFailure(message: String)  extends OptionsResult


object Options {

  type OptionMap = Map[Symbol, Any]

  /// Parses the command line arguments.
  def parse(args: List[String]) : OptionsResult = {

    def isSwitch(s : String) = s.startsWith("-")

    // process the arguments recursively
    def process(m : OptionMap, xs: List[String]) : OptionMap = xs match {
        case Nil => m
        case "--someflag" :: v :: tail  => process(m ++ Map('someflag -> v.toInt), tail)
        case "-someswitch" :: v :: tail => process(m ++ Map('someswitch -> v), xs.tail)
        case "-tempDir" :: v :: tail => process(m ++ Map('someswitch -> v), xs.tail)
        case arg :: _                   => process(m ++ Map('arg -> arg), xs.tail)
    }

    args match {
      case List() => OptionsFailure("Usage: Please provide required options...!")
      case _      => createOptions(process(Map(), args.map(_.toLowerCase)))
    }
  }

  def createOptions(map: OptionMap): OptionsSuccess = {
    val options = new Options {
      val archivePath = " "
      val tempDir = ""
      val logDir = ""
      val whatIf = false
    }
    OptionsSuccess(options)
  }

}

/// The command line options that can be provided to the importer.
abstract class Options {
  val archivePath:  String
  val tempDir: String
  val logDir: String
  val whatIf: Boolean
}

