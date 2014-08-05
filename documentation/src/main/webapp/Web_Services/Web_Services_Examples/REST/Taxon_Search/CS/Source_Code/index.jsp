<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>TaxonSearch and Taxonomy Source Code C#</h1>
        <h2>TaxonSearch ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="TaxonSearch.aspx.cs" Inherits="NBNAPI.REST.TaxonSearch" %>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Taxon Search</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <form id="form1" runat="server">
            <div>
                <h1>Taxon Search</h1>
                Search for a taxon: 
                <asp:TextBox ID="txtSearch" runat="server"></asp:TextBox>
                <asp:Button ID="btnSearch" runat="server" OnClick="btnSearch_Click" Text="Search" />
                <div>
                    <asp:ListView ID="lvResults" runat="server">
                        <LayoutTemplate>
                            <p runat="server" id="itemPlaceHolder">
                            </p>
                        </LayoutTemplate>
                        <EmptyDataTemplate>
                            No results found
                        </EmptyDataTemplate>
                        <ItemTemplate>
                            <p>
                                <asp:LinkButton ID="lnkTaxonomy" runat="server" PostBackUrl='<\%# "~/REST/Taxonomy.aspx?taxonVersionKey=" + Eval("taxonVersionKey") %>'><\%#Eval("name") %></asp:LinkButton>
                            </p>
                        </ItemTemplate>
                    </asp:ListView>
                </div>
            </div>
            </form>
        </body>
        </html>
        ]]></script>
        <h2>TaxonSearch Code behind</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;
        using System.Web;

        namespace NBNAPI.REST
        {
            public partial class TaxonSearch : System.Web.UI.Page
            {
                NBNClient _client;

                protected void Page_Load(object sender, EventArgs e)
                {
                    _client = new NBNClient();
                }

                protected void btnSearch_Click(object sender, EventArgs e)
                {
                    var searchResults = _client.GetTaxonSearchResult(HttpUtility.HtmlEncode(txtSearch.Text));
                    lvResults.DataSource = searchResults.results;
                    lvResults.DataBind();

                }
            }
        }
        ]]></script>

        <h2>Taxonomy ASPX Markup</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <\%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Taxonomy.aspx.cs" Inherits="NBNAPI.REST.Taxonomy" %>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Taxonomy</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <form id="form1" runat="server">
            <div>
                <h2>Taxonomy for <asp:Label ID="lblTaxonName" runat="server"></asp:Label></h2>
                <h2>Parents:</h2>
                <asp:ListView ID="lvParents" runat="server">
                    <EmptyDataTemplate>
                        No parents
                    </EmptyDataTemplate>
                    <LayoutTemplate>
                        <p runat="server" id="itemPlaceHolder"></p>
                    </LayoutTemplate>
                    <ItemTemplate>
                        <p>
                            <asp:Label ID="lblRank" runat="server" Text='<\%# Eval("rank") %>'></asp:Label>
                            :&nbsp;
                            <asp:Label ID="lblParentName" runat="server" Text='<\%# Eval("name") %>'></asp:Label>
                        </p>
                    </ItemTemplate>
                </asp:ListView>
                <h2>Children:</h2>
                <asp:ListView ID="lvChildren" runat="server">
                    <EmptyDataTemplate>
                        No children
                    </EmptyDataTemplate>
                    <LayoutTemplate>
                        <p runat="server" id="itemPlaceHolder"></p>
                    </LayoutTemplate>
                    <ItemTemplate>
                        <p>
                            <asp:Label ID="lblChildName" runat="server" Text='<\%# Eval("name") %>'></asp:Label>
                        </p>
                    </ItemTemplate>
                </asp:ListView>
            </div>
            </form>
        </body>
        </html>
        ]]></script>
        <h2>Taxonomy Code behind</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;

        namespace NBNAPI.REST
        {
            public partial class Taxonomy : System.Web.UI.Page
            {
                NBNClient _client;
                protected void Page_Load(object sender, EventArgs e)
                {
                    string taxonVersionKey = Request.QueryString["taxonversionkey"];
                    _client = new NBNClient();
                    Taxon taxon = _client.GetTaxonomy(taxonVersionKey);
                    lblTaxonName.Text = taxon.name;

                    if (taxon.taxonVersionKey != taxon.ptaxonVersionKey)
                    {
                        Taxon preferredTaxon = _client.GetTaxonomy(taxon.ptaxonVersionKey);
                        lblTaxonName.Text += " (" + preferredTaxon.name + ")";
                    }

                    Taxon[] parents = _client.GetTaxonParents(taxonVersionKey);
                    lvParents.DataSource = parents;
                    lvParents.DataBind();

                    Taxon[] children = _client.GetTaxonChildren(taxonVersionKey);
                    lvChildren.DataSource = children;
                    lvChildren.DataBind();
                }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
