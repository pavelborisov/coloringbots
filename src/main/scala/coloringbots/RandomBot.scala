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
class RandomBot extends Bot{
//  override def action: Option[] = Option(new Coord (1,2))
  implicit def tuple2coord(tuple: (Int, Int)): Cell = field.get(Coord(tuple._1, tuple._2)).get
  val random = new Random

  //todo
  override def nextTurn: Turn = {
    for (i <- 1 until 20) {
      def turn = this -> (x,y)
      if ( turn.validate) return turn
    }
    this -> (x,y)
  }

  override def notify(cell: Cell) = {}

  override def color: String = "None"

  override var field: Field = null

  private def x = random.nextInt(field.size.x)
  private def y = random.nextInt(field.size.y)
}