package coloringbots

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher extends App{
  Game(Coord(5, 6), 10)
    .register(RandomBot("blue"))
    .register(RandomBot("red"))
    .register(RandomBot("green"))
    .register(RandomBot("yellow"))
    .register(new PrintingBot).play
}
