package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result
import uk.org.nbn.nbnv.importer.data.Repository

class MetadataValidator (repo: Repository) {
  def validate(metadata: Metadata):  List[Result]  = {
    val results = new ListBuffer[Result]

    //validate that the dataset key is valid for existing datasets
    val v0 = new Nbnv604Validator(repo)
    results.append(v0.validate(metadata))

    //validate that the administrator email is valid for new datasets
    val v1 = new Nbnv600Validator(repo)
    results.append(v1.validate(metadata))

    results.toList
  }

}
