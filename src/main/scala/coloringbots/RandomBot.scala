package coloringbots

import scala.util.Random

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:37
 * Since: 
 *
 */
case class RandomBot(val color: String) extends Bot{
  val random = new Random

  //todo
  override def nextTurn: Turn = 1 to 20 map turn find validate getOrElse turn(21)

  override def notify(cell: Cell) = {}
  override var field: Field = null

  private def x = random.nextInt(field.size.x)
  private def y = random.nextInt(field.size.y)
  private def validate(turn: Turn) = turn.validate
  private def turn(i: Int): Turn = this -> (x, y)
}