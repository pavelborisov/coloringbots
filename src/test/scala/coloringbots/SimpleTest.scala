package coloringbots

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:23
 */

/*
 * Поскольку Gradle запускает тесты для всех приложений java, то приходится оборачивать
 * тесты scala в jUnit.
 */
@RunWith(classOf[JUnitRunner])
class SimpleTest extends FunSuite {

  test("Simple test example. 0 should be equal 0") {
    assert(0 == 0)
  }

}
