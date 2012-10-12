package uk.org.nbn.nbnv.importer.data

import com.google.inject.Inject

class KeyGenerator @Inject()(db: Database) {

  def nextTaxonDatasetKey = {

    val latest = db.repo.getLatestDatasetKey

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
