<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon Source Code C#</h1>
        <h2>RecordsForPolygonMap JavaScript</h2>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false"><![CDATA[
        var vectorLayer;
        var proj4326 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");

        function loadmap() {
            map = new OpenLayers.Map({ div: 'map' });

            var osmLayer = new OpenLayers.Layer.OSM("OpenStreetMap");
            osmLayer.isBaseLayer = true;
            map.addLayer(osmLayer);

            vectorLayer = new OpenLayers.Layer.Vector();
            map.addLayer(vectorLayer);
            var polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon);
            map.addControl(polygonControl);
            polygonControl.activate();

            var hfWKT = document.getElementById("hfWKT");
            if (hfWKT.value != "") {
                var feature = new OpenLayers.Feature.Vector();
                feature.geometry = OpenLayers.Geometry.fromWKT(hfWKT.value);
                feature.geometry.transform(proj4326, proj900913);
                vectorLayer.addFeatures([feature]);
                map.zoomToExtent(vectorLayer.getDataExtent());
            } else {
                //an example extent of reasonable dimensions
                var bounds = new OpenLayers.Bounds(-3.65, 51.6, -2.60, 52.0);
                bounds.transform(proj4326, proj900913);
                map.zoomToExtent(bounds);
            }

            vectorLayer.events.register("featureadded", vectorLayer, onFeatureAdded);
            vectorLayer.events.register("sketchcomplete", vectorLayer, onSketchComplete);
        }

        function onFeatureAdded(e) {
            var format = new OpenLayers.Format.WKT();
            //transform in place so clone geometry
            var transformedGeom = e.feature.geometry.clone();
            transformedGeom.transform(proj900913, proj4326);
            var transformedFeature = new OpenLayers.Feature.Vector(transformedGeom);
            document.getElementById("hfWKT").value = format.write(transformedFeature);
        }

        function onSketchComplete(e) {
            vectorLayer.removeAllFeatures();
        }
        ]]></script>
        <h1>RecordsForPolygon</h1>
        <h2>ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="RecordsForPolygon.aspx.cs" Inherits="NBNAPI.REST.RecordsForPolygon" %>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Records For A Custom Polygon</title>
            <link rel="stylesheet" href="../css/style.css" />
            <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js">&lt;/script>
            <script type="text/javascript" src="../js/RecordsForPolygonMap.js">&lt;/script>
        </head>
        <body onload="loadmap()">
            <form id="form1" runat="server">
            <div>
                <asp:HiddenField ID="hfWKT" runat="server" ClientIDMode="Static" />
                <h1>Records from the BSBI Vascular Plants Database for a Custom Polygon</h1>

                <div id="map"></div>

                <div>
                    <p>
                        Select a designation:
                        <asp:DropDownList ID="ddlDesignation" runat="server" DataTextField="name" DataValueField="code"></asp:DropDownList>
                    </p>
                    <p>
                        StartYear: <asp:TextBox ID="txtStartYear" runat="server"></asp:TextBox>
                        EndYear: <asp:TextBox ID="txtEndYear" runat="server"></asp:TextBox>
                        <asp:Button ID="btnRefresh" runat="server" ClientIDMode="Static" OnClick="btnRefresh_Click" Text="Refresh" />
                    </p>
                </div>
                <div>
                    <asp:ListView ID="lvRecords" runat="server">
                        <LayoutTemplate>
                            <table class="nbnData">
                                <thead>
                                    <th>DatasetKey</th>
                                    <th>Observation ID</th>
                                    <th>Observation Key</th>
                                    <th>Location</th>
                                    <th>Resolution</th>
                                    <th>TaxonVersionKey</th>
                                    <th>Preferred TaxonVersionKey</th>
                                    <th>Preferred Taxon Name</th>
                                    <th>Preferred Taxon Authority</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Date Type</th>
                                </thead>
                                <tbody>
                                    <tr runat="server" id="itemPlaceHolder"></tr>
                                </tbody>
                            </table>
                        </LayoutTemplate>
                        <EmptyDataTemplate>
                            No records
                        </EmptyDataTemplate>
                        <ItemTemplate>
                            <tr>
                                <td>
                                    <asp:HyperLink ID="hlDataset" runat="server" NavigateUrl='<\%# "https://data.nbn.org.uk/Datasets/" + Eval("datasetKey") %>'><\%#Eval("datasetKey") %></asp:HyperLink>
                                </td>
                                <td>
                                    <\%#Eval("observationID") %>
                                </td>
                                <td>
                                    <\%#Eval("observationKey") %>
                                </td>
                                <td>
                                    <\%#Eval("location") %>
                                </td>
                                <td>
                                    <\%#Eval("resolution") %>
                                </td>
                                <td>
                                    <\%#Eval("taxonVersionKey") %>
                                </td>
                                <td>
                                    <\%#Eval("pTaxonVersionKey") %>
                                </td>
                                <td>
                                    <\%#Eval("pTaxonName") %>
                                </td>
                                <td>
                                    <\%#Eval("pTaxonAuthority") %>
                                </td>
                                <td>
                                    <\%#Eval("startDate") %>
                                </td>
                                <td>
                                    <\%#Eval("endDate") %>
                                </td>
                                <td>
                                    <\%#Eval("dateTypeKey") %>
                                </td>
                            </tr>
                        </ItemTemplate>
                    </asp:ListView>
                </div>
            </div>
            </form>
        </body>
        </html>
        ]]></script>
        <h2>Code behind</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;
        using System.Web.UI;

        namespace NBNAPI.REST
        {
            public partial class RecordsForPolygon : System.Web.UI.Page
            {
                NBNClient _client = new NBNClient();

                protected void Page_Load(object sender, EventArgs e)
                {
                    if (!Page.IsPostBack)
                    {
                        txtStartYear.Text = "1800";
                        txtEndYear.Text = DateTime.Now.Year.ToString();
                        ddlDesignation.DataSource = _client.GetDesignations();
                        ddlDesignation.DataBind();
                        ddlDesignation.SelectedValue = "BAP-2007";
                    }
                }

                protected void btnRefresh_Click(object sender, EventArgs e)
                {
                    string wkt = hfWKT.Value;
                    string designation = ddlDesignation.SelectedValue;
                    int startYear = Int32.Parse(txtStartYear.Text);
                    int endYear = Int32.Parse(txtEndYear.Text);
                    string datasetKey = "GA000091";
                    _client.Login("#username#", "#password#");
                    lvRecords.DataSource = _client.PostRecordsForWKTDesignationDatasetDates(wkt, designation, datasetKey, startYear, endYear);
                    lvRecords.DataBind();
                }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
