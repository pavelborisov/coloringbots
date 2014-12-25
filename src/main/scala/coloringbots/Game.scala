package coloringbots

import scala.util.Try
import Utils._

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
  protected var bot: Option[Bot] = None

  private def x: Int = coord.x
  private def y: Int = coord.y
  private def get(x: Int, y: Int) = field.get(x -> y)

  override def whose   = bot
  override def left  = get(x - 1, y    )
  override def right = get(x + 1, y    )
  override def up    = get(x    , y - 1)
  override def down  = get(x    , y + 1)
  override def neighbours: Set[Cell] = N + up.left + up + up.right + left + right + down.left + down + down.right
  override def isEmpty: Boolean = whose.isEmpty

  def set(bot: Bot): Boolean = { this.bot = Option(bot); println(s"$coord marked ${bot.color}"); true }
}

case class FieldImpl(override val size: Coord) extends Field{
  override val cells = Array.ofDim[Cell](size.x, size.y)
  override def get(coord: Coord): Option[Cell] = Try(cells(coord.x)(coord.y)).toOption

  def put(cell: CellImpl, bot: Bot): Boolean = {cell.set(bot)}
}

class Round(val bots: Bots){
  def run(i: Int)    = bots foreach turn
  def turn(bot: Bot) = bot paint cell or disqualify

  private def disqualify(bot: Bot) = {bots disqualify bot; None}
  private def cell(bot: Bot): Turn = bot.nextTurn

  def turn2(bot: Bot) = this paint bot map validate map make getOrElse disqualify(bot)
  private def validate(turn: Turn) = if (turn.validate) turn else throw new IllegalStateException("Некорректный ход у бота " + turn.bot)
  private def make(turn: Turn) = turn.cell.asInstanceOf[CellImpl].set(turn.bot)
  private def paint(bot: Bot) = Try(bot nextTurn)
}

case class Game (size: Coord, rounds: Int) {
  private val field = FieldImpl(size)
  private val bots = new Bots

  def register(bot: Bot) = { bots register bot; bot.field = field; this }

  def play = {
    0 until rounds foreach round
    this
  }

  private def round(i: Int) = {
    bots run i
  }
}

