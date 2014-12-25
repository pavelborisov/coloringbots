package coloringbots

import scala.collection.mutable
import scala.util.Try

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 25.12.14
 * Time: 10:57
 *
 */
trait TurnMaker {
  def bot: Bot
  def paint(f: (Bot)=> Turn): TurnMaker = this
  def or(f: Bot => Unit) = {}
}

class TurnMakerGood(override val bot: Bot) extends TurnMaker{
  override def paint(foo: (Bot)=> Turn): TurnMaker = {
    this bar foo map validate map make getOrElse bad
  }

  private def validate(turn: Turn): Turn = if (turn.validate) turn else exception
  private def exception = throw new IllegalStateException("Некорректный ход у бота " + bot)
  private def bad = new TurnMakerBad(bot)
  private def make(turn: Turn) = {turn.cell.asInstanceOf[CellImpl].set(turn.bot); this}
  private def bar(foo: (Bot)=> Turn) = Try(foo(bot))
}

class TurnMakerBad(override val bot: Bot) extends TurnMaker{
  override def or(f: Bot => Unit) = f(bot)
}

class Bots{
  private val bots = new mutable.HashSet[Bot]
  private val losers = new mutable.HashSet[Bot]

  private def isActive(bot: Bot) = !(losers contains bot)

  def register(bot: Bot): Bots = { bots += bot; this }
  def disqualify(bot: Bot) = losers += bot

  def players: Seq[Bot] = bots filter isActive toSeq
  def all: Seq[Bot] = bots toSeq
  def foreach(turn: (Bot) => Unit) = players foreach turn

}

case class Neighbours(private val set: Set[Cell]){
  def +(f: => Cell): Neighbours = Try(f).toOption.fold(this){c =>Neighbours(set + c)}
  def toSet = set.toSet
}

object Utils {
  implicit def bots2Round(bots: Bots): Round = new Round(bots)
  implicit def bot2TurnMaker(bot: Bot):TurnMaker = new TurnMakerGood(bot)
  implicit def option2Cell(o: Option[Cell]): Cell = o.get
  implicit def neighbours2Set(n: Neighbours): Set[Cell] = n.toSet
  implicit def tuple2Coord(t: (Int, Int)): Coord = Coord(t._1, t._2)
  def N = Neighbours(Set())
}

