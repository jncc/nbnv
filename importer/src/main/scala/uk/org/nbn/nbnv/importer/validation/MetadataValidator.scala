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

    //valdiate that the import type is specified in the metadata
    val v2 = new Nbnv899Validator
    results.append(v2.validate(metadata))

    //validate the dataset title
    val v3 = new Nbnv951Validator
    results.append(v3.validate(metadata))

    //validate the administrator email
    val v4 = new Nbnv953Validator
    results.append(v4.validate(metadata))

    //validate the org name exists
    val v5 = new Nbnv763Validator(repo)
    results.append(v5.validate(metadata))

    results.toList
  }

}
