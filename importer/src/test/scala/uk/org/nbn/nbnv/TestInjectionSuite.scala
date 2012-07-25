package uk.org.nbn.nbnv

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.google.inject.{Inject, AbstractModule, Guice}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestInjectionSuite extends FunSuite with ShouldMatchers {

  test("google guice injection framework should work") {

    val injector = Guice.createInjector(new TestInjectionModule)
    val dependee = injector.getInstance(classOf[Dependee])
    val result = dependee.bar(3)
    result should be (12)
  }
}

class Dependent {
  def foo(n: Int) = n * n
}

class Dependee @Inject()(dependent: Dependent) {
  def bar(n: Int) = dependent.foo(n) + n
}

class TestInjectionModule extends AbstractModule {
  @Override
  protected def configure() {
    // looks like self-bindings are automatic in guice
//    bind(classOf[Dependent]).to(classOf[Dependent]) // causes error
  }
}

