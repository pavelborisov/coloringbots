package coloringbots

import coloringbots.bots._

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher extends App{
  private val timer = new TimeZombi
  Game(Coord(9, 9), 50)
    .register(ImmortalBot("blue"))
    .register(RandomBot("red"))
    .register(RandomBot("green"))
    .register(SeqBot("yellow"))
    .register(SeqBotV2("orange"))
    .register(ChampionBot("pink"))
    .register(ClanBot("magenta"))
    .register(PrintZombi)
    .register(timer)
    .play
  println(timer)
}
