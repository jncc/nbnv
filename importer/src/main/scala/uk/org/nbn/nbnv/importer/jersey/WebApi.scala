package uk.org.nbn.nbnv.importer.jersey

import com.sun.jersey.api.client.{Client, ClientResponse, WebResource}
import com.sun.jersey.core.util.MultivaluedMapImpl
import javax.ws.rs.core.{NewCookie, MediaType}
import scala.collection.JavaConversions._
import uk.org.nbn.nbnv.importer.Settings
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.json._


class WebApi() {
  val config = new DefaultClientConfig()
  config.getFeatures()
    .put(JSONConfiguration.FEATURE_POJO_MAPPING, true)

  val client = Client.create(config)

  val webResource = client.resource(Settings.apiUrl)

  private def login() : NewCookie = {
    val formData = new MultivaluedMapImpl()

    formData.add("username", Settings.apiUser)
    formData.add("password", Settings.apiPassword)

    val acResponse = webResource.path("/user/login").`type`(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(classOf[ClientResponse], formData);

    if (acResponse.getStatus != 200) {
      val message = "Authentication response was %s".format(acResponse.getStatus)
      throw new Exception(message)
    }

    val cookies  = acResponse.getCookies.find(c => c.getName == "nbn.token_key")

    cookies match {
      case None => throw new Exception("Couldn't authenticate")
      case Some(c) => c
    }
  }

  private def logout(authCookie: NewCookie) {
    val logOutResponse = webResource.path("/user/logout").cookie(authCookie).accept(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])

    if (logOutResponse.getStatus != 200) {
      val message = "Logout response was %s".format(logOutResponse.getStatus)
      throw new Exception(message)
    }
  }

  def resetDatasetAccess(datasetKey : String) {
    val authCookie = login()

    val oaPath = "/organisation/organisationAccesses/reset/%s".format(datasetKey)
    val oaResponse = webResource.path(oaPath).cookie(authCookie).accept(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])
    if (oaResponse.getStatus != 200) throw new Exception("Error reseting organisation access. Api response code was %s".format(oaResponse.getStatus))

    val uaPath = "/user/userAccesses/reset/%s".format(datasetKey)
    val uaResponse = webResource.path(uaPath).cookie(authCookie).accept(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])
    if (uaResponse.getStatus != 200) throw new Exception("Error reseting user access. Api response code was %s".format(uaResponse.getStatus))

    logout(authCookie)
  }
}
