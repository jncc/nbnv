package uk.org.nbn.nbnv.importer.testing

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar

/// A base class for tests.

@RunWith(classOf[JUnitRunner])
class BaseFunSpec extends FunSuite with ShouldMatchers with MockitoSugar
