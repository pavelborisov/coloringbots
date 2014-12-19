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
class CellImpl (val coord: Coord, val field: Field) extends Cell{
  override def get   = None           //todo
  override def right = None           //todo
  override def left  = None          //todo
  override def down  = None          //todo
  override def up    = None          //todo
  override def neighbours: Set[Cell] = Set[Cell]()//todo

  def set(bot: Bot)  = { println(s"$coord marked ${bot.color}") } //todo
}

class FieldImpl(val size: Coord) extends Field{
  val cells = Array.ofDim[Cell](size.x, size.y)
  override def get(coord: Coord): Option[Cell] = Try(cells(coord.x)(coord.y)).toOption
  def put(cell: CellImpl, bot: Bot) = {cell.set(bot)}
}

object FieldImpl{
  def apply(size: Coord) = new FieldImpl(size)
}

class Manager (val size: Coord, val iterations: Int) {
  private val field = FieldImpl(size)
  private val bots = new mutable.HashSet[Bot]

  def add(bot: Bot) = { bots += bot; bot.field = field; this }

  def execute = {
    0 until iterations foreach iteration
  }

  def iteration(i: Int) = {
    bots foreach action
  }

  def action(bot: Bot) = {
    bot.action.foreach(put(_, bot))
  }

  def put(cell: Cell, bot: Bot) = {
    field.put(cell.asInstanceOf[CellImpl], bot)
    bots.foreach(_.notify(cell))
  }
}
