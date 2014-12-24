package coloringbots

import scala.collection.mutable
import scala.util.Try

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:43
 * Since: 
 *
 */
/*
object CellImpl{

}
 */

case class CellImpl (override val coord: Coord, override val field: Field) extends Cell{
  import  CellObj._
  protected var bot: Option[Bot] = None

  private def x: Int = coord.x
  private def y: Int = coord.y
  private def get(x: Int, y: Int) = field.get(x -> y)

  override def who   = bot  //todo bad name
  override def left  = get(x - 1, y    )
  override def right = get(x + 1, y    )
  override def up    = get(x    , y - 1)
  override def down  = get(x    , y + 1)
  override def neighbours: Set[Cell] = N + up.left + up + up.right + left + right + down.left + down + down.right
  override def isEmpty: Boolean = who.isEmpty

  //todo write rule for check assigning of bot
  def set(bot: Bot): Boolean = { this.bot = Option(bot); println(s"$coord marked ${bot.color}"); true }
}

class CellRule(cell: Cell, bot: Bot) {
  private def isBlank: Boolean = false
  private def canFill: Boolean = false
  private def canOver: Boolean = false

  private def canPaint = isBlank || canFill || canOver

  def paint = if (canPaint) cell.asInstanceOf[CellImpl].set(bot) else throw new IllegalStateException("")
}



/*
class GameRule{
  def disqualify(bot: Bot) = {}
  def nextTurn: Turn = {}
}
*/

case class FieldImpl(override val size: Coord) extends Field{
  override val cells = Array.ofDim[Cell](size.x, size.y)
  override def get(coord: Coord): Option[Cell] = Try(cells(coord.x)(coord.y)).toOption

  def put(cell: CellImpl, bot: Bot): Boolean = {cell.set(bot)}
}

trait TurnMaker {
  def bot: Bot
  def paint(f: (Bot)=> Turn): TurnMaker = this
  def or(f: Bot => Unit) = {}
}

class TurnMakerGood(override val bot: Bot) extends TurnMaker{
  override def paint(foo: (Bot)=> Turn): TurnMaker = Try(foo(bot)).map(validate _).getOrElse(bad)

  private def validate(turn: Turn): TurnMaker = if (turn.validate) this else exception
  private def exception = throw new IllegalStateException("Некорректный ход у бота ")// + bot)
  private def bad = new TurnMakerBad(bot)
}

class TurnMakerBad(override val bot: Bot) extends TurnMaker{
  override def or(f: Bot => Unit) = f(bot)
}

/*
  def getTurn(bot: Bot) = {
    val turn = bot.nextTurn
    if (!turn.validate) throw new IllegalStateException("Некорректный ход у бота " + bot)
    turn
  }
*/
//class
class Round(val bots: Bots){
  implicit def bot2TurnMaker(bot: Bot):TurnMaker = new TurnMakerGood(bot)

  def run(i: Int) = bots foreach turn

  //    Try(bot nextTurn).map(validate _).map(make _).getOrElse(bots disqualify bot)
  def turn(bot: Bot) = {

    bot paint cell or disqualify

  }

  private def validate(turn: Turn) = if (turn.validate) turn else exception
  private def exception = throw new IllegalStateException("Некорректный ход у бота ")// + bot)
  private def make(turn: Turn) = turn.cell.asInstanceOf[CellImpl].set(turn.bot)
  private def disqualify(bot: Bot): Unit = {bots disqualify bot; None}
  private def cell(bot: Bot): Turn = bot.nextTurn

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

//todo rename to Game
class Manager (val size: Coord, val rounds: Int) {
  private val field = FieldImpl(size)
  private val bots = new Bots

  def add(bot: Bot) = { bots register bot; bot.field = field; this }

  def play = {
    0 until rounds foreach round
  }

  private def round(i: Int) = {
    bots run i
  }

  //todo
/*
  private def makeTurn(bot: Bot) = {
    if ( Try( bot.nextTurn.makeIt action.exists(put(bot)) ).getOrElse( false ) )
        losers += bot
  }

  private def put(bot: Bot)(cell: Cell): Boolean = {
    if (field.put(cell.asInstanceOf[CellImpl], bot) )
      {bots.foreach(_.notify(cell)); true }
    else
      false
  }
*/
}

private object CellObj {
  implicit def option2Cell(o: Option[Cell]): Cell = o.get
  implicit def neighbours2Set(n: Neighbours): Set[Cell] = n.toSet
  implicit def tuple2Coord(t: (Int, Int)): Coord = Coord(t._1, t._2)
  def N = Neighbours(Set())
}

private case class Neighbours(private val set: Set[Cell]){
  def +(f: => Cell): Neighbours = Try(f).toOption.fold(this){c =>Neighbours(set + c)}
  def toSet = set.toSet
}

private object Bots {
  implicit def bots2Round(bots: Bots): Round = new Round(bots)
}

