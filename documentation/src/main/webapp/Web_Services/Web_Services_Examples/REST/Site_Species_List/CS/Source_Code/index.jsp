<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List Source Code C#</h1>
        <h2>ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="SiteSpeciesList.aspx.cs" Inherits="NBNAPI.REST.SiteSpeciesList" %>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Species List for a Vice County</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <form id="form1" runat="server">
            <div>
                <h1>Species List for a Vice County</h1>
                <asp:DropDownList ID="ddlViceCounties" runat="server" DataTextField="name" DataValueField="identifier"></asp:DropDownList>
                <asp:DropDownList ID="ddlTaxonOutputGroups" runat="server" DataTextField="name" DataValueField="key"></asp:DropDownList>
                <asp:DropDownList ID="ddlDesignations" runat="server" DataTextField="name" DataValueField="code" AppendDataBoundItems="true">
                    <asp:ListItem Text="" Value=""></asp:ListItem>
                </asp:DropDownList>
                <asp:Button ID="btnRefresh" runat="server" OnClick="btnRefresh_Click" Text="Refresh" />
                <h2><asp:Label ID="lblResults" runat="server"></asp:Label></h2>
                <asp:ListView ID="lvSpecies" runat="server">
                    <LayoutTemplate>
                        <h3>Species List</h3>
                        <table class="nbnData">
                            <thead>
                                <th>Taxon Name</th>
                                <th>Taxon Authority</th>
                                <th>TaxonVersionKey</th>
                                <th>Taxon Group</th>
                                <th>Rank</th>
                                <th>Number of records</th>
                            </thead>
                            <tr runat="server" id="itemPlaceHolder"></tr>
                        </table>
                    </LayoutTemplate>
                    <EmptyDataTemplate>
                        <h3>Species List</h3>
                        No records
                    </EmptyDataTemplate>
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
                                <th>DatasetKey</th>
                                <th>Title</th>
                                <th>Data Provider</th>
                                <th>Number of records</th>
                            </thead>
                            <tr runat="server" id="itemPlaceHolder"></tr>
                        </table>
                    </LayoutTemplate>
                    <EmptyDataTemplate>
                        <h3>Dataset List</h3>
                        No records
                    </EmptyDataTemplate>
                    <ItemTemplate>
                        <tr>
                            <td>
                                <asp:HyperLink ID="hlDataset" runat="server" NavigateUrl='<\%# "https://data.nbn.org.uk/Datasets/" + Eval("datasetKey") %>'><\%#Eval("datasetKey") %></asp:HyperLink>
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

        namespace NBNAPI.REST
        {
                public partial class SiteSpeciesList : System.Web.UI.Page
                {
                        NBNClient _client;

                        protected void Page_Load(object sender, EventArgs e)
                        {
                                _client = new NBNClient();
                                if (!IsPostBack)
                                {
                                        var viceCounties = _client.GetViceCounties();
                                        ddlViceCounties.DataSource = viceCounties;
                                        ddlViceCounties.DataBind();

                                        var taxonOutputGroups = _client.GetTaxonOutputGroups();
                                        ddlTaxonOutputGroups.DataSource = taxonOutputGroups;
                                        ddlTaxonOutputGroups.DataBind();

                        var designations = _client.GetDesignations();
                        ddlDesignations.DataSource = designations;
                        ddlDesignations.DataBind();
                                }
                        }

                        protected void btnRefresh_Click(object sender, EventArgs e)
                        {
                                var species = _client.GetSiteSpecies(ddlViceCounties.SelectedValue, ddlTaxonOutputGroups.SelectedValue, ddlDesignations.SelectedValue);
                    lvSpecies.DataSource = species;
                    lvSpecies.DataBind();

                    var datasets = _client.GetSiteDatasets(ddlViceCounties.SelectedValue, ddlTaxonOutputGroups.SelectedValue, ddlDesignations.SelectedValue);
                    lvDatasets.DataSource = datasets;
                    lvDatasets.DataBind();
                        }
                }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
