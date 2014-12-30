package coloringbots.bots

import coloringbots._

import scala.collection
import scala.collection.immutable.Stack
import scala.collection.parallel.mutable
import scala.util.Random

/**
  * Created by IntelliJ IDEA.
  * User: Andrew F. Podkolzin
  * Date: 26.12.14
  * Time: 17:43
  * Since:
  *
  */
case class SeqBotV2(val color: String) extends Bot{

  val random = new Random
  var cellMove = List[Cell]()

  lazy val init = {
    var x:Int = 0
    var y:Int = 0

    do {
      x = random.nextInt(field.size.x + 1)
      y = random.nextInt(field.size.y + 1)
    } while( !field.get( Coord(x,y) ).get.isEmpty )

    cellMove = field.get( Coord(x,y) ).get :: cellMove
  }

  override def nextTurn: Turn = {
    init

    cellMove.foreach (
    ({ last_cell:Cell =>
      List( last_cell.up, last_cell.down, last_cell.left, last_cell.right).foreach(
        ({ cell:Option[Cell] => if( cell.isDefined && cell.get.isEmpty ) {
            cellMove = cell.get :: cellMove
            val turn = Turn(this, cell.get)
            if (turn.validate)
              return turn
          }
        }) (_)
      ) }) (_) )

    this->(0,0)
  }

  override def notify(cell: Cell) = {}
  override var field: Field = null
}