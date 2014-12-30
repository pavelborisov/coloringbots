package coloringbots

import coloringbots.bots.{SeqBotV2, SeqBot, RandomBot}

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
    .register(SeqBot("yellow"))
    .register(SeqBotV2("orange"))
    .register(ChampionBot("white"))
    .register(PrintZombi)
    .register(timer)
    .play
    .register(SeqBot("pink")).play
  println(timer)
}
