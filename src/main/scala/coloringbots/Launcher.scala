package coloringbots

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher extends App{
  private val timer: TimeZombi = new TimeZombi
  Game(Coord(5, 6), 15)
    .register(RandomBot("blue"))
    .register(RandomBot("red"))
    .register(RandomBot("green"))
    .register(SeqBot("yellow"))
    .register(PrintZombi)
    .register(timer)
    .play
  println(timer)
}
