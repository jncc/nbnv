<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonOutputGroup Source Code Java</h1>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class TaxonOutputGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String name;
	private String parentTaxonGroupKey;
	private int sortOrder;


	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParentTaxonGroupKey() {
		return parentTaxonGroupKey;
	}
	public void setParentTaxonGroupKey(String parentTaxonGroupKey) {
		this.parentTaxonGroupKey = parentTaxonGroupKey;
	}
	
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
