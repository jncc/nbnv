package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Organisation
import uk.org.nbn.nbnv.importer.data.Repository
import com.google.inject.Inject

class OrganisationIngester @Inject()(repository: Repository) {

  def ensureOrganisation(name: String) = {

    repository.getOrganisation(name) match {

      case Some(o) => o
      case None    => {
        // todo create /insert organisation
        throw new UnsupportedOperationException
//        val o = new Organisation
//        o
      }
    }
  }}
