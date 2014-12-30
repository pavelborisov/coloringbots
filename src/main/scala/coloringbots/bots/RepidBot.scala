package coloringbots.bots

import java.util

import coloringbots.{Coord, Field, Cell, Turn}

/**
 * Created by Павел on 31.12.2014.
 */
class RepidBot {
  val cellMap:Seq[Coord]=Seq[Int,Coord]

  lazy val init = {
    var i = 0
    while( i < field.size.x ) {
      var j = 0
      while( j < field.size.y ) {
        val coord = Coord(i,j)
        field.get( coord ) match {
          case Some(cell) if cell.isEmpty
        }
          cellMap.add( coord )
      }
    }
  }

  override def nextTurn: Turn = 1 to 20 map turn find validate get

  override def notify(cell: Cell) = {}
  override var field: Field = null

  private def x = random.nextInt(field.size.x + 1)
  private def y = random.nextInt(field.size.y + 1)
  private def validate(turn: Turn) = turn.validate
  private def turn(i: Int): Turn = this -> (x, y)
}
