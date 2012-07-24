package uk.org.nbn.nbnv

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestMetadataReader extends FunSuite with ShouldMatchers {
  test("can read xml") {
    val validXml = <eml xmlns="eml://ecoinformatics.org/eml-2.1.1" xmlns:dc="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" packageId="NBN/GA000159/eml-1.xml" scope="system" system="GBIF" xml:lang="en" xsi:schemaLocation="eml://ecoinformatics.org/eml-2.1.1 eml.xsd">
                    <dataset>
                      <alternateIdentifier> Test Identifier </alternateIdentifier>
                      <title xml:lang="en"> Test Title </title>
                      <abstract>
                        <para> Test Description </para>
                      </abstract>
                      <intellectualRights>
                        <para>Access Constraints: Test Access constraints Use Constraints: Test Use Constraints </para>
                      </intellectualRights>
                      <coverage>
                        <geographicCoverage>
                          <geographicDescription> Test geographic coverage </geographicDescription>
                        </geographicCoverage>
                      </coverage>
                      <purpose>
                        <para> Test purpose </para>
                      </purpose>
                      <methods>
                        <methodStep>
                          <description>
                            <para> Test Method </para>
                          </description>
                        </methodStep>
                        <qualityControl>
                          <description>
                            <para> Test Quality </para>
                          </description>
                         </qualityControl>
                      </methods>      
                   </dataset>
                  </eml>
    
    println(validXml)
    
    val testReader = new MetadataReader()
    val testResult = testReader.GetMetaData(validXml)
    
    testResult.accessConstraints should be ("Test Access constraints")
    testResult.datasetKey should be ("Test Identifier")
    testResult.datsetTitle should be ("Test Title")
    testResult.description should be ("Test Description")
    testResult.geographicCoverage should be ("Test geographic coverage")
    testResult.method should be ("Test Method")
    testResult.quality should be ("Test Quality")
    testResult.purpose should be ("Test purpose")
    testResult.useConstraints should be ("Test Use Constraints")
  }
}
