package coloringbots

import coloringbots.bots._
import coloringbots.zombies.{TimeZombi, PrintZombi}

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher extends App{
  private val timer = new TimeZombi
  Game(Coord(14, 14), 100)
    .register(RandomBot("red"))
    .register(ImmortalBot("blue"))
    .register(SeqBot("yellow"))
    .register(SeqBotV2("orange"))
    .register(ChampionBot("pink"))
    .register(RapidBot("white"))
    .register(ClanBot("magenta"))
    .register(BotBegemot("dart"))
    .register(PrintZombi)
    .register(timer)
    .play
  timer.print
}
