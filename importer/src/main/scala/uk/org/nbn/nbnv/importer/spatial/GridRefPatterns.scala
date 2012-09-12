package uk.org.nbn.nbnv.importer.spatial

object GridRefPatterns {
  val ukGridRef             = """(?i)^[HNOST][A-Z](\d\d)*$"""
  val ukDintyGridRef        = """(?i)^[HNOST][A-Z]\d{2}[A-NP-Z]$"""
  val irishGridRef          = """(?i)^[A-HJ-Z](\d\d)*$"""
  val channelIslandsGridRef = """(?i)^[W][A-Z](\d\d)*$"""
}
