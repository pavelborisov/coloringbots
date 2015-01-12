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
  Game(Coord(10, 10), 500)
    .register(RandomBot("red"))
    .register(ImmortalBot("blue"))
    .register(SeqBot("yellow"))
    .register(SeqBotV2("orange"))
    .register(ChampionBot("pink"))
    .register(RapidBot("white"))
    .register(ClanBot("café clan"))
    .register(BotBegemot("dart"))
    .register(XoBot("xoxo"))
    .register(NearestBot("neon nearest"))
    .register(StupidBot("magenta"))
    .register(PrintZombi)
    .register(timer)
    .play
//  timer.print
}
