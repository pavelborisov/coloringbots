package coloringbots.bots

import java.util

import coloringbots._

import scala.util.Random

/**
 * Created by ����� on 31.12.2014.
 */
case class RapidBot(val color: String) extends Bot {
  val random = new Random
  val cellMap:collection.mutable.ArrayBuffer[(Int,Int)]= collection.mutable.ArrayBuffer.empty[(Int,Int)]

  lazy val init = {
    var i:Int = 0
    while( i < field.size.x ) {
      var j:Int = 0
      while( j < field.size.y ) {
        field.get(Coord(i, j)) match {
          case Some(cell) if cell.isEmpty =>
            cellMap += ((i, j))
          case _ =>
        }
        j=j+1
      }
      i=i+1
    }
  }

  override def nextTurn: Turn = {
    if( cellMap.isEmpty) init

    val (x:Int,y:Int) = cellMap( random.nextInt( cellMap.size ) )

    val t = this -> (x,y)
    if( !t.validate )
    {
      (0 to field.size.y).foreach {y=>
        (0 to field.size.x).foreach{ x =>
          val t: Turn = this ->(x, y)
          if (t.validate)
            return t
        }
      }
    }

    t
  }

  override def notify(cell: Cell) = {
    traverse( cell )
    cell.neighbours.foreach(traverse)

    def traverse(cell:Cell) = {
      if ( !cell.whose.contains(this) ) {
        val turn = Turn(this, cell)
        if (turn.validate)
          cellMap += ((cell.coord.x, cell.coord.y))
      }
    }
  }

  override var field: Field = null
}
