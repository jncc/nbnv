package uk.org.nbn.nbnv.importer.jersey

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.json.JSONConfiguration
import uk.org.nbn.nbnv.importer.Settings


object WebResourceFactory {
  def getWebResource() = {
    val config = new DefaultClientConfig()
    config.getFeatures()
      .put(JSONConfiguration.FEATURE_POJO_MAPPING, true)

    val client = Client.create(config)

    client.resource(Settings.apiUrl)
  }

}
