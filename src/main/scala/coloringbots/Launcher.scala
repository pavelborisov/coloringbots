package coloringbots

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher {
  def main(args: Array[String]): Unit = {
    //TODO: make it run
    Game(Coord(10, 10), 5).register(new RandomBot).register(new PrintingBot).play
  }
}
