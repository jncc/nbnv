<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<archive xmlns="http://rs.tdwg.org/dwc/text/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" metadata="eml.xml" xsi:schemaLocation="http://rs.tdwg.org/dwc/text/ http://rs.tdwg.org/dwc/text/tdwg_dwc_text.xsd">
<core encoding="UTF-8" fieldsEnclosedBy="" fieldsTerminatedBy="\t" ignoreHeaderLines="1" linesTerminatedBy="\r\n" rowType="http://rs.tdwg.org/dwc/terms/Occurrence">
<files>
<location>data.tab</location>
</files>
<#list darwinCoreFields as field>
<field index="${field.index}" term="${field.term}"/>
</#list>
<id index="${recordKeyCol}"/>
</core>
<extension encoding="UTF-8" fieldsEnclosedBy="" fieldsTerminatedBy="\t" ignoreHeaderLines="1" linesTerminatedBy="\r\n" rowType="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence">
<files>
<location>data.tab</location>
</files>
<#list nbnExtensionFields as field>
<field index="${field.index}" term="${field.term}"/>
</#list>
<coreid index="${recordKeyCol}"/>
</extension>
</archive>
