<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h2>ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="TenKmRecords.aspx.cs" Inherits="NBNAPI.REST.TenKmRecords" %>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Records for a Ten Km Square</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <form id="form1" runat="server">
            <div>
                <h1>Records for a Ten Km Square</h1>
                <h3>Ten Km Square: TL28</h3>
                <h3>Dataset: <a href="https://data.nbn.org.uk/Datasets/GA000466">GA000466</a>: Demonstration dataset for record access on the NBN Gateway</h3>
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
                            <tr runat="server" id="itemPlaceHolder"></tr>
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
            </form>
        </body>
        </html>
        ]]></script>
        <h2>Code behind</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;

        namespace NBNAPI.REST
        {
            public partial class TenKmRecords : System.Web.UI.Page
            {
                NBNClient _client;

                protected void Page_Load(object sender, EventArgs e)
                {
                    _client = new NBNClient();
                    _client.Login("#username#", "#password#");
                    //GA000466 in square TL28
                    lvRecords.DataSource = _client.PostRecordsForDatasetAndGridRef("GA000466", "TL28");
                    lvRecords.DataBind();

                    //records come back with a featureID (instead of a grid ref) you can get the details of the featue with, for example, https://data.nbn.org.uk/api/features/165399
                }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
