package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result
import uk.org.nbn.nbnv.importer.data.Repository

class MetadataValidator (repo: Repository) {
  def validate(metadata: Metadata):  List[Result]  = {
    val results = new ListBuffer[Result]

    val v0 = new Nbnv600Validator(repo)
    results.append(v0.validate(metadata))

    results.toList
  }

}
