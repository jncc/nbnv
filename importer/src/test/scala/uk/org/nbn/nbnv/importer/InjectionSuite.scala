package uk.org.nbn.nbnv.importer

import com.google.inject.{Inject, AbstractModule, Guice}
import testing.BaseFunSuite

class InjectionSuite extends BaseFunSuite {

  test("google guice injection framework should work") {

    val singleton = new SingletonDependent

    class TestInjectionModule extends AbstractModule {
      @Override
      protected def configure() {
        // looks like self-bindings are automatic in guice
        //    bind(classOf[Dependent]).to(classOf[Dependent]) // causes error
        bind(classOf[SingletonDependent]).toInstance(singleton)
      }
    }

    val injector = Guice.createInjector(new TestInjectionModule)
    val dependee = injector.getInstance(classOf[Dependee])

    val result = dependee.bar(3)
    result should be(12)

    val dependee2 = injector.getInstance(classOf[Dependee])
    dependee.singleton should be (dependee2.singleton)
  }
}

class Dependent {
  def foo(n: Int) = n * n
}

class SingletonDependent

class Dependee @Inject() (dependent: Dependent, val singleton: SingletonDependent) {
  def bar(n: Int) = dependent.foo(n) + n
}

// ---

//class User
//
//
//trait UserRepositoryComponent {
//  val userRepository = new UserRepository
//  class UserRepository {
//    def auth(user: User) = {
//      // authenticating...
//      user
//    }
//  }
//
//}
//
//trait UserServiceComponent { this: UserRepositoryComponent =>
//  val userService = new UserService
//  class UserService {
//    def authenticate(username: String) = {
//      userRepository.auth(username)
//    }
//  }
//
//}




