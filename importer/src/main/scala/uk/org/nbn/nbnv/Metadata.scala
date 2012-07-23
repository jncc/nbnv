/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv

abstract class Metadata {
  val datasetKey: String
  val datsetTitle: String
  val description: String
  val accessConstraints: String
  val useConstraints: String
  val geographicCoverage: String
  val purpose: String
  val method: String
  val quality: String
  
  override def toString = datasetKey
}
