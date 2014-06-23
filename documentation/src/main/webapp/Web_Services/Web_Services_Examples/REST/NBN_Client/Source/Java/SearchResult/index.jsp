<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>SearchResult and Result Source Code Java</h1>
<h2>SearchResult</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class SearchResult implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Result[] results;
    private Header header;
    
    public Result[] getResults() {
    	return results;
    }
    public void setResults(Result[] results) {
		this.results = results;
	}
    
    public Header getHeader() {
    	return header;
    }
    public void setHeader(Header header) {
    	this.header = header;
    }
}
]]></script>
<h2>Result</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class Result implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String entityType;
	private String searchMatchTitle;
	private String descript;
	private String pExtendedName;
	private String taxonVersionKey;
	private String name;
	private String authority;
	private String languageKey;
    private String taxonOutputGroupKey;
    private String taxonOutputGroupName;
    private String commonNameTaxonVersionKey;
    private String commonName;
    private String organismKey;
    private String rank;
    private String nameStatus;
    private String versionForm;
    private int gatewayRecordCount;
    private String href;
    private String ptaxonVersionKey;
    
    public String getEntityType() {
    	return entityType;
    }
    public void setEntityType(String entityType) {
    	this.entityType = entityType;
    }
    
    public String getSearchMatchTitle() {
    	return searchMatchTitle;
    }
    public void setSearchMatchTitle(String searchMatchTitle) {
    	this.searchMatchTitle = searchMatchTitle;
    }
    
    public String getDescript() {
    	return descript;
    }
    public void setDescript(String descript) {
    	this.descript = descript;
    }
    
    public String getpExtendedName() {
    	return pExtendedName;
    }
    public void setpExtendedName(String pExtendedName) {
    	this.pExtendedName = pExtendedName;
    }
    
    public String getTaxonVersionKey () {
    	return taxonVersionKey;
    }
    public void setTaxonVersionKey(String taxonVersionKey) {
    	this.taxonVersionKey = taxonVersionKey;		
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
    
    public String getLanguageKey() {
    	return languageKey;
    }
    public void setLanguageKey(String languageKey) {
    	this.languageKey = languageKey;
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
    
    public String getCommonNameTaxonVersionKey() {
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
    
    public String getOrganismKey() {
    	return organismKey;
    }
    public void setOrganismKey(String organismKey) {
    	this.organismKey = organismKey;
    }
    
    public String getRank() {
    	return rank;
    }
    public void setRank(String rank) {
    	this.rank = rank;
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
    
    public String getHref() {
    	return href;
    }
    public void setHref(String href) {
    	this.href = href;
    }
    
    public String getPtaxonVersionKey() {
    	return ptaxonVersionKey;
    }
    public void setPTaxonVersionKey(String ptaxonVersionKey) {
    	this.ptaxonVersionKey = ptaxonVersionKey;
    } 
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
