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
  var cellMove = new collection.mutable.ArrayStack[Cell]()

  lazy val init = {
    var x:Int = 0
    var y:Int = 0

    do {
      x = random.nextInt(field.size.x + 1)
      y = random.nextInt(field.size.y + 1)
    } while( !field.get( Coord(x,y) ).get.isEmpty )

    cellMove.push( field.get( Coord(x,y) ).get )
  }

  type cellop = ()=>Option[Cell]
  def cells(cell:Cell):List[cellop] = {
    val c = cell.coord
    List[cellop](
      { () => cell.up },
      { () => cell.down },
      { () => cell.right },
      { () => cell.left },
      { () => field.get( Coord(c.x+1,c.y+1) ) },
      { () => field.get( Coord(c.x+1,c.y-1) ) },
      { () => field.get( Coord(c.x-1,c.y+1) ) },
      { () => field.get( Coord(c.x-1,c.y-1) ) })
  }

  override def nextTurn: Turn = {
    init

    cellMove.foreach ( cells(_).foreach { fcell =>
    //cellMove.foreach { last_cell =>
    //  last_cell.neighbours.foreach { cell =>
        val cellopt = fcell()
        if( !cellopt.isEmpty ) {
          val cell = cellopt.get
          val turn = Turn(this, cell)
          if (turn.validate) {
            cellMove.push(cell)
            return turn
          }
        }
      } )
    //}

    throw new Exception("No more turn!!!")
  }

  override def notify(cell: Cell) = {}
  override var field: Field = null
}