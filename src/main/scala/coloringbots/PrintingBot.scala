package coloringbots

import scala.collection.immutable.IndexedSeq

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 25.12.14
 * Time: 18:20
 * Since: 
 *
 */
class PrintingBot extends Bot
{
  override def notify(cell: Cell): Unit = this print field
  override def nextTurn: Turn = throw new UnsupportedOperationException()
  override def color: String = "None"
  override var field: Field = null

  private def print(field: Field): Unit = {
    (0 to field.size.y).reverse.foreach{y=>
      (0 to field.size.x).map(x => (x, y).whose).foreach(print)
      println()
    }
  }

  private def print(bot: Option[Bot]): Unit = Predef.print(bot.map(_.color).getOrElse(" "))
}
