package uk.org.nbn.nbnv.importer.spatial

object GridRefPatterns {
  val ukGridRef                  = """(?i)^[HJNOST][A-Z](\d\d)+$"""
  val ukDintyGridRef             = """(?i)^[HJNOST][A-Z]\d{2}[A-NP-Z]$"""
  val irishGridRef               = """(?i)^[A-HJ-Z](\d\d)+$"""
  val irishDintyGrid             = """(?i)^[A-HJ-Z]\d{2}[A-NP-Z]$"""
  val channelIslandsGridRef      = """(?i)^W[AV](\d\d)+$"""
  val channelIslandsDintyGridRef = """(?i)^W[AV]\d{2}[A-NP-Z]$"""
}
