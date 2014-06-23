<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten Km Species List Source Code C#</h1>
        <a href="../../../NBN_Client/CS/Source_Code" target="_blank"><h2>(NBN Client Source Code)</h2></a>
        <h2>ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="TenKmSpeciesList.aspx.cs" Inherits="NBNAPI.REST.TenKmSpeciesList" %>
        <!DOCTYPE html>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Species List for a Ten Km Square</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <form id="form1" runat="server">
            <div>
                <h1>Species List for a Ten Km Square</h1>
                <h3>Ten Km Square: SO21</h3>
                <asp:ListView ID="lvSpecies" runat="server">
                                <LayoutTemplate>
                        <h3>Species List</h3>
                                        <table class="nbnData">
                            <thead>
                                <tr>
                                    <th>Taxon Name</th>
                                    <th>Taxon Authority</th>
                                    <th>TaxonVersionKey</th>
                                    <th>Taxon Group</th>
                                    <th>Rank</th>
                                    <th>Number of records</th>
                                </tr>
                            </thead>
                            <tr runat="server" id="itemPlaceHolder"></tr>
                        </table>
                                </LayoutTemplate>
                                <ItemTemplate>
                        <tr>
                            <td>
                                <\%#Eval("taxon.name") %>
                            </td>
                            <td>
                                <\%#Eval("taxon.authority") %>
                            </td>
                            <td>
                                <\%#Eval("taxon.taxonVersionKey") %>
                            </td>
                            <td>
                                <\%#Eval("taxon.taxonOutputGroupName") %>
                            </td>
                            <td>
                                <\%#Eval("taxon.rank") %>
                            </td>
                            <td>
                                <\%#Eval("querySpecificObservationCount") %>
                            </td>
                        </tr>
                                </ItemTemplate>
                        </asp:ListView>

                <asp:ListView ID="lvDatasets" runat="server">
                                <LayoutTemplate>
                        <h3>Dataset List</h3>
                                        <table class="nbnData">
                            <thead>
                                <tr>
                                    <th>DatasetKey</th>
                                    <th>Title</th>
                                    <th>Data Provider</th>
                                    <th>Number of records</th>
                                </tr>
                            </thead>
                            <tr runat="server" id="itemPlaceHolder"></tr>
                        </table>
                                </LayoutTemplate>
                                <ItemTemplate>
                        <tr>
                            <td>
                                <asp:HyperLink ID="hlDataset" runat="server" NavigateUrl='<\%#Eval("taxonDataset.href") %>'><\%#Eval("datasetKey") %></asp:HyperLink>
                            </td>
                            <td>
                                <\%#Eval("taxonDataset.title") %>
                            </td>
                            <td>
                                <\%#Eval("taxonDataset.organisationName") %>
                            </td>
                            <td>
                                <\%#Eval("querySpecificObservationCount") %>
                            </td>
                        </tr>
                                </ItemTemplate>
                        </asp:ListView>
            </div>
            </form>
        </body>
        </html>
        ]]></script>
        <h2>Code behind</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;
        using System.IO;
        using System.Net;
        using System.Web.Script.Serialization;

        namespace NBNAPI.REST
        {
                public partial class TenKmSpeciesList : System.Web.UI.Page
                {
                NBNClient _client;

                        protected void Page_Load(object sender, EventArgs e)
                        {
                    _client = new NBNClient();

                    lvSpecies.DataSource = _client.GetTenKmSpecies("SO21");
                    lvSpecies.DataBind();

                    lvDatasets.DataSource = _client.GetTenKmDatasets("SO21");
                    lvDatasets.DataBind();
                        }
                }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
