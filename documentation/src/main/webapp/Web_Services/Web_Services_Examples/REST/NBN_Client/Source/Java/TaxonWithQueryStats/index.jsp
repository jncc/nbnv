<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonWithQueryStats and Taxon Source Code Java</h1>
<h2>TaxonWithQueryStats</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class TaxonWithQueryStats implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String taxonVersionKey;
	private int querySpecificObservationCount;
	private Taxon taxon;
	
	public String getTaxonVersionKey() {
		return taxonVersionKey;
	}
	public void setTaxonVersionKey(String taxonVersionKey) {
		this.taxonVersionKey = taxonVersionKey;
	}
	
	public int getQuerySpecificObservationCount() {
		return querySpecificObservationCount;
	}
	public void setQuerySpecificObservationCount(int querySpecificObservationCount) {
		this.querySpecificObservationCount = querySpecificObservationCount;
	}
	
	public Taxon getTaxon() {
		return taxon;
	}
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}
}
]]></script>
<h2>Taxon</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class Taxon implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String taxonVersionKey;
	private String organismKey;
	private String name;
	private String authority;
	private String rank;
	private String taxonOutputGroupKey;
	private String taxonOutputGroupName;
	private String ptaxonVersionKey;
	private String commonNameTaxonVersionKey;
	private String commonName;
	private String href;
	private String languageKey;
	private String nameStatus;
	private String versionForm;
	private int gatewayRecordCount;

	public String getTaxonVersionKey() {
		return taxonVersionKey;
	}
	public void setTaxonVersionKey(String taxonVersionKey) {
		this.taxonVersionKey = taxonVersionKey;
	}
	
	public String getOrganismKey() {
		return organismKey;
	}
	public void setOrgansimKey(String organismKey) {
		this.organismKey = organismKey;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getTaxonOutputGroupKey() {
		return taxonOutputGroupKey;
	}
	public void setTaxonOutputGroupKey(String taxonOutputGroupKey) {
		this.taxonOutputGroupKey = taxonOutputGroupKey;
	}
	
	public String getTaxonOutputGroupName() {
		return taxonOutputGroupName;
	}
	public void setTaxonOutputGroupName(String taxonOutputGroupName) {
		this.taxonOutputGroupName = taxonOutputGroupName;
	}

	public String getPtaxonVersionKey() {
		return ptaxonVersionKey;
	}
	public void setPtaxonVersionKey(String PtaxonVersionKey) {
		this.ptaxonVersionKey = PtaxonVersionKey;
	}
	
	public String getCommonNameVersionKey() {
		return commonNameTaxonVersionKey;
	}
	public void setCommonNameTaxonVersionKey(String commonNameTaxonVersionKey) {
		this.commonNameTaxonVersionKey = commonNameTaxonVersionKey;
	}
	
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	public String languageKey() {
		return languageKey;
	}
	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}
	
	public String getNameStatus() {
		return nameStatus;
	}
	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}
	
	public String getVersionForm() {
		return versionForm;
	}
	public void setVersionForm(String versionForm) {
		this.versionForm = versionForm;
	}
	
	public int getGatewayRecordCount() {
		return gatewayRecordCount;
	}
	public void setGatewayRecordCount(int gatewayRecordCount) {
		this.gatewayRecordCount = gatewayRecordCount;
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
