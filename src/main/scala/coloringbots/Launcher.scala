package coloringbots

import coloringbots.bots._

/**
 * User: rgordeev
 * Date: 19.12.14
 * Time: 16:29
 */
object Launcher extends App{
  Game(Coord(5, 6), 15)
    .register(ImmortalBot("blue"))
    .register(ImmortalBot("white"))
    .register(RandomBot("red"))
    .register(RandomBot("green"))
    .register(SeqBot("yellow"))
    .register(SeqBotV2("orange"))
    .register(new PrintingBot)
    .play
}
