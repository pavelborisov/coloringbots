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
  override def notify(cell: Cell): Unit = this print cell
  override def nextTurn: Turn = throw new UnsupportedOperationException()
  override def color: String = "printer"
  override var field: Field = null

  private def print(cell: Cell): Unit = {
    (0 to field.size.y).reverse.foreach{y=>
      (0 to field.size.x).map(x => (x, y)).foreach(c => print(char(c, cell)))
      println()
    }
  }

  private def print(c: Char): Unit = Predef.print(c + "\t")
  private def char(i: Cell, cell: Cell): Char = {
    color(i).map(c =>
      if (i.coord == cell.coord)
        c.toUpperCase
      else
        c.toLowerCase)
      .getOrElse("_")(0)
  }
  private def color(i: Cell): Option[String] = i.whose.map(_.color)
}
