package uk.org.nbn.nbnv.importer.testing

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar

/// A base class for tests.

@RunWith(classOf[JUnitRunner])
class BaseFunSpec extends FunSpec with ShouldMatchers with MockitoSugar
