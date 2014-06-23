<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>SiteBoundary Source Code Java</h1>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class SiteBoundary implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int featureID;
	private String name;
	private String providerKey;
	private String description;
	private String siteBoundaryDatasetKey;
	private int siteBoundaryCategoryId;
	private String identifier;
	
	public int getFeatureID() {
		return featureID;
	}
	public void setFeatureID(int featureID) {
		this.featureID = featureID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getProviderKey() {
		return providerKey;
	}
	public void setProviderKey(String providerKey) {
		this.providerKey = providerKey;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSiteBoundaryDatasetKey() {
		return siteBoundaryDatasetKey;
	}
	public void setSiteBoundaryDatasetKey(String siteBoundaryDatasetKey) {
		this.siteBoundaryDatasetKey = siteBoundaryDatasetKey;
	}
	
	public int getSiteBoundaryCategoryId() {
		return siteBoundaryCategoryId;
	}
	public void setSiteBoundaryCategoryId(int siteBoundaryCategoryID) {
		this.siteBoundaryCategoryId = siteBoundaryCategoryID;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
