package uk.org.nbn.nbnv.utility

import xml.XML

class FileSystem {
  def loadXml(path: String) = XML.load(path)
}
