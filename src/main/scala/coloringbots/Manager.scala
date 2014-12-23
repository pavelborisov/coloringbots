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

  override def get   = bot
  override def left  = get(x - 1, y    )
  override def right = get(x + 1, y    )
  override def up    = get(x    , y - 1)
  override def down  = get(x    , y + 1)
  override def neighbours: Set[Cell] = N + up.left + up + up.right + left + right + down.left + down + down.right

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

class GameRule{
  def nextTurn = {}

}

case class FieldImpl(override val size: Coord) extends Field{
  override val cells = Array.ofDim[Cell](size.x, size.y)
  override def get(coord: Coord): Option[Cell] = Try(cells(coord.x)(coord.y)).toOption

  def put(cell: CellImpl, bot: Bot): Boolean = {cell.set(bot)}
}

class Manager (val size: Coord, val iterations: Int) {
  private val field = FieldImpl(size)
  private val bots = new mutable.HashSet[Bot]
  private val losers = new mutable.HashSet[Bot]

  def add(bot: Bot) = { bots += bot; bot.field = field; this }

  def execute = {
    0 until iterations foreach iteration
  }

  private def iteration(i: Int) = {
    bots filter isActive foreach actionBot
  }

  private def isActive(bot: Bot) = !(losers contains bot)

  private def actionBot(bot: Bot) = {
    if ( Try( bot.action.exists(put(bot)) ).getOrElse( false ) )
        losers += bot
  }

  private def put(bot: Bot)(cell: Cell): Boolean = {
    if (field.put(cell.asInstanceOf[CellImpl], bot) )
      {bots.foreach(_.notify(cell)); true }
    else
      false
  }
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

