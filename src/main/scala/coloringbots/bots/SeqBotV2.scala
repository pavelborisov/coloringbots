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
  var cellMove = new collection.mutable.ArrayStack[Cell]

  lazy val init = {
    var x:Int = 0
    var y:Int = 0

    do {
      x = random.nextInt(field.size.x + 1)
      y = random.nextInt(field.size.y + 1)
    } while( !field.get( Coord(x,y) ).get.isEmpty )

    cellMove.push( field.get( Coord(x,y) ).get )
  }

  type cellop = (Coord)=>(Int,Int)
  val cells:List[cellop] = {
    List[cellop](
      { c:Coord => ( c.x + 3, c.y + 3) },
      { c:Coord => ( c.x - 3, c.y + 3) },
      { c:Coord => ( c.x + 3, c.y - 3) },
      { c:Coord => ( c.x - 3, c.y - 3) },

      { c:Coord => ( c.x + 1, c.y + 2) },
      //{ c:Coord => ( c.x, c.y + 2) },
      { c:Coord => ( c.x - 1, c.y + 2) },
      { c:Coord => ( c.x + 1, c.y - 2) },
      //{ c:Coord => ( c.x, c.y - 2) },
      { c:Coord => ( c.x - 1, c.y - 2) },
      { c:Coord => ( c.x + 2, c.y + 1) },
      //{ c:Coord => ( c.x + 2, c.y) },
      { c:Coord => ( c.x + 2, c.y - 1) },
      { c:Coord => ( c.x - 2, c.y + 1) },
      //{ c:Coord => ( c.x - 2, c.y) },
      { c:Coord => ( c.x - 2, c.y - 1) },

      { c:Coord => ( c.x + 1, c.y + 1) },
      { c:Coord => ( c.x - 1, c.y + 1) },
      { c:Coord => ( c.x + 1, c.y - 1) },
      { c:Coord => ( c.x - 1, c.y - 1) },
      { c:Coord => ( c.x + 1, c.y ) },
      { c:Coord => ( c.x - 1, c.y ) },
      { c:Coord => ( c.x, c.y + 1 ) },
      { c:Coord => ( c.x, c.y - 1) })
  }

  val cash:collection.mutable.HashSet[Int] = collection.mutable.HashSet.empty[Int]

  override def nextTurn: Turn = {
    init

    cash.clear()

    cellMove.foreach { last_cell =>
      cash.add { last_cell.coord.x * 1000 + last_cell.coord.y }

      cells.foreach { coord =>
         val (x,y) = coord( last_cell.coord );
         val key = { x * 1000 + y  }
         if( !cash.contains(key) &&
           x >= 0 && x <= field.size.x &&
           y >= 0 && y <= field.size.y )
         {
           val cell = field.get(Coord(x, y)).get
           val turn = Turn(this, cell)
           if (turn.validate) {
             cellMove.push(cell)
             return turn
           }
        }
        else
          cash.add(key)
      }
    }

    // check another move
    (field.size.y to 0 by -1).foreach {y=>
      (field.size.x to 0 by -1).foreach{ x =>
        val t: Turn = this ->(x, y)
        if (t.validate)
          return t
      }
    }

    throw new Exception("No more turn!!!")
  }

  override def notify(cell: Cell) = {}
  override var field: Field = null
}