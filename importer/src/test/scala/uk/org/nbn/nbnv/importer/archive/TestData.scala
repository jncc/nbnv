package uk.org.nbn.nbnv.importer.archive

object TestData {
  val validMetadataXml =
  <archive xmlns="http://rs.tdwg.org/dwc/text/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" metadata="eml.xml" xsi:schemaLocation="http://rs.tdwg.org/dwc/text/ http://rs.tdwg.org/dwc/text/tdwg_dwc_text.xsd">
    <core encoding="UTF-8" fieldsEnclosedBy="" fieldsTerminatedBy="\t" ignoreHeaderLines="1" linesTerminatedBy="\r\n" rowType="http://rs.tdwg.org/dwc/terms/Occurrence">
      <files>
        <location>data.tab</location>
      </files>
      <id index="0"/>
      <field index="0" term="http://rs.tdwg.org/dwc/terms/occurrenceID"/>
      <field index="1" term="http://rs.tdwg.org/dwc/terms/collectionCode"/>
      <field index="2" term="http://rs.tdwg.org/dwc/terms/eventID"/>
      <field index="6" term="http://rs.tdwg.org/dwc/terms/taxonID"/>
      <field index="8" term="http://rs.tdwg.org/dwc/terms/locationID"/>
      <field index="9" term="http://rs.tdwg.org/dwc/terms/locality"/>
      <field index="13" term="http://rs.tdwg.org/dwc/terms/recordedBy"/>
      <field index="14" term="http://rs.tdwg.org/dwc/terms/identifiedBy"/>
      <field index="18" term="http://rs.tdwg.org/dwc/terms/dynamicProperties"/>
      <field index="19" term="http://rs.tdwg.org/dwc/terms/verbatimSRS"/>
      <field index="20" term="http://rs.tdwg.org/dwc/terms/verbatimLongitude"/>
      <field index="21" term="http://rs.tdwg.org/dwc/terms/verbatimLatitude"/>
      <field index="22" term="http://rs.tdwg.org/dwc/terms/eventDate"/>
      <field index="23" term="http://rs.tdwg.org/dwc/terms/occurrenceStatus"/>
    </core>
    <extension encoding="UTF-8" fieldsEnclosedBy="" fieldsTerminatedBy="\t" ignoreHeaderLines="1" linesTerminatedBy="\r\n" rowType="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence">
      <files>
        <location>data.tab</location>
      </files>
      <coreid index="0"/>
      <field index="3" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart"/>
      <field index="4" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd"/>
      <field index="5" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode"/>
      <field index="7" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence"/>
      <field index="10" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType"/>
      <field index="11" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference"/>
      <field index="12" term="http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecision"/>
    </extension>
  </archive>
}
