import scala.collection.mutable

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:43
 * Since: 
 *
 */
class CellImpl (val coord: Coord) extends Cell{
  override def get: Option[Bot] = None //todo
  override def set(bot: Bot): Option[Bot] = None//todo
}

class FieldImpl(val size: Coord) extends Field{
  val cells = mutable.Seq[CellImpl]()
  override def get(coord: Coord): Option[Cell] = None //todo
  def put(coord: Coord, color: String) = {println(s"$coord marked $color");None} //todo
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
    bots foreach action _
  }

  def action(bot: Bot) = {
    bot.action.foreach(field.put(_, bot.color))
  }



}
