package uk.org.nbn.nbnv.importer.data

class KeyGenerator(repository: TaxonDatasetRepository) {

  def nextTaxonDatasetKey = {

    val latest = repository.getLatestTaxonDatasetKey

    latest match {
      case Some(key) => {
        val number = key.substring(2).toInt
        val incremented = number + 1
        val padded = "%6s".format(incremented.toString).replace(' ', '0')
        "GA" + padded
      }
      case None => "GA000001"
    }
  }
}
