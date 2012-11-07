using System;
using System.Data;
using System.Configuration;
using System.Collections;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using System.Net;
using net.searchnbn.webservice;

/// <summary>
/// Jon Cooper 22nd July 2008
/// This is an example of doing digest authentication using the Gateway web services.
/// This is the c# code behind for the asp.net webform DigestExample.aspx.
/// The form has 4 text boxes (txtDatasetKey, txtTaxonVersionKey, txtUsername, txtPassword),
/// a submit button and a blank image for the map (imgMap).
/// Provide a taxonVersionKey that has sensitive records and also the corresponding dataset key
/// The username and password are then used to authenticate the user against the secure web services.
/// 
/// </summary>
public partial class DigestExample : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
    }

    protected void Button1_Click(object sender, EventArgs e){
        //This is the address of the Gateway's secure web services
        string _secureUrl = "http://www.nbnws.net/ws/secure/webservice";

        //Get the web service and set its credentials and url
        GatewayWebService _gws = new GatewayWebService();
        _gws.Url = _secureUrl;
        _gws.Credentials = getCredentials(_secureUrl);

        //Create a grid map request
        GridMapRequest _gmr = new GridMapRequest();

        //A species with sensitive records, value from the webform
        _gmr.TaxonVersionKey = txtTaxonVersionKey.Text;

        //Add a dataset filter for the sensitive records, value from the webform
        string[] _datasets = {txtDatasetKey.Text};
        DatasetFilter _df = new DatasetFilter();
        _df.DatasetKey = _datasets;
        _gmr.DatasetList = _df;

        //Set the image size
        GridMapSettings _gms = new GridMapSettings();
        _gms.Width = 400;
        _gms.WidthSpecified = true;
        _gms.Height = 400;
        _gms.HeightSpecified = true;
        _gmr.GridMapSettings = _gms;

        //Get the data
        GridMap _gm = _gws.GetGridMap(_gmr);

        //Add the map to the page
        imgMap.ImageUrl = _gm.Map.Url;
    }

    /// <summary>
    /// Returns a CredentialCache set to digest authentication for the username/password from the form
    /// The username and password are from the webform
    /// </summary>
    /// <returns></returns>
    private CredentialCache getCredentials(string _secureUrl)
    {
        CredentialCache cc = new CredentialCache();
        NetworkCredential nc = new NetworkCredential(txtUsername.Text, txtPassword.Text);
        cc.Add(new Uri(_secureUrl), "Digest", nc);
        return cc;
    }

}
