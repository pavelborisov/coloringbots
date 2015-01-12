package coloringbots.zombies

import coloringbots.{Bot, Cell, Field, Turn}

import scala.collection.mutable

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 25.12.14
 * Time: 18:20
 * Since: 
 *
 */
object PrintZombi extends Bot
{
  override def notify(cell: Cell): Unit = this print cell
  override def nextTurn: Turn = throw new UnsupportedOperationException()
  override def color: String = "printer"
  override var field: Field = null


  private def print(cell: Cell): Unit = {
    val counter = new Counter

    (0 to field.size.y).reverse.foreach{y=>
      (0 to field.size.x).map(x => (x, y)).foreach(c => print(char(c, cell, counter)))
      println()
    }
    println(counter)
  }

  private def print(c: Char): Unit = Predef.print( (if( c.isUpper ) "*"+c+"*" else c ) + "\t")
  private def char(i: Cell, cell: Cell, counter: Counter): Char = {
    i.whose.foreach(counter.inc)

    color(i).map(c =>
      if (i.coord == cell.coord)
        c.toUpperCase
      else
        c.toLowerCase)
      .getOrElse("_")(0)
  }
  private def color(i: Cell): Option[String] = i.whose.map(_.color)
}

class Counter {
  private val map = new mutable.HashMap[Bot, Int]()

  def inc(bot: Bot) = map(bot) = map.getOrElse(bot, 0) + 1

  override def toString: String = map.toList.sortBy{ case (bot, i) => if (bot.color.equals("magenta")) "" else bot.color }.toString
}