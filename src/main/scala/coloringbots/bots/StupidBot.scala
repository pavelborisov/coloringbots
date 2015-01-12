package coloringbots.bots

import coloringbots._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Pavel on 03.01.2015.
 */
case class StupidBot(val color: String) extends Bot
{
  override var field: Field = null

  /* Оповещение о закрашивании ячейки cell */
  override def notify(cell: Cell): Unit = {
    init

    val c = cell.coord;
    val i = c.x
    val j = c.y

    val idx = player2Idx.getOrElseUpdate( cell.whose.get, { playerCount = playerCount + 1; playerCount - 1 })
    val (enemy, whose) = fieldArray(i)(j)
    //val my = cell.whose.contains( this )
    //val enemy = enemya(idx)

//    val own_koef = if(my) 1 else if(whose == 1) -1 else 0
//    val enemy_koef = if(!my) 1 else 0

    Seq((i - 1, j + 1), (i - 1, j), (i - 1, j - 1), (i, j + 1), (i, j - 1), (i + 1, j + 1), (i + 1, j), (i + 1, j - 1)).foreach {
      case (x,y) if (x >= 0 && x <= field.size.x && y >= 0 && y <= field.size.y) => {
          val (cenemy,cwhose) = fieldArray(x)(y)

          cenemy(idx) = cenemy(idx) + 1
          cenemy(whose) = cenemy(whose) - 1

          //if( my )
          fieldArray(x)(y) = (cenemy, cwhose)//(cown + own_koef, cenemy + enemy_koef, cwhose)
        }
      case _ =>
    }

    //fieldArray(i)(j) = (own + own_koef, enemy + enemy_koef, if (my) 1 else -1)
    enemy(whose) = enemy(whose) - 1
    enemy(idx) = enemy(idx) + 1
    fieldArray(i)(j) = ( enemy, idx)
    fieldSum = sum

    println( s"sum:${fieldSum}")
/*
    println( "notify:\n" +
      fieldArray.map { _.map { case (a, i) => a.map { a => if(a == 0) "-" else a }.mkString(",") + f":$i%02d" }.mkString(" ") }
      .mkString("\n"))
*/
  }

    /* Возвращает следующий ход */
  override def nextTurn: Turn = {
    init

    var x = 0
    var y = 0
    var max = 0.0d

    var i:Int = 0

    println("Next turn:")
    while( i <= field.size.x ) {
      var j:Int = 0
      while( j <= field.size.y ) {
        val (cells,whose) = fieldArray(i)(j)
        val enemy = cells.slice(2, cells.size)

        //val enemyMax = enemy.max
        val enemyCount = enemy.foldLeft(0){ (sum,i) => sum + (if( i > 0 ) 1 else 0 )}
        val own = cells(1)
        //val cell = field.get(Coord(i,j))

        //println( s"${i}:${j}=${fieldSum} - ${cells}:${enemyCount}:${own}:${whose}")
        if( whose != 1 && ( own > 2 || ( whose == 0 && own > 1) ||
          (whose == 0 && enemyCount == 0 ))) {

          var currentSum = fieldSum
          Seq(
            (i - 1, j + 1),
            (i - 1, j),
            (i - 1, j - 1),
            (i, j + 1),
            (i, j - 1),
            (i + 1, j + 1),
            (i + 1, j),
            (i + 1, j - 1),
            (i - 2, j + 1),
            (i - 2, j),
            (i - 2, j - 1),
            (i, j + 2),
            (i, j - 2),
            (i + 2, j + 1),
            (i + 2, j),
            (i + 2, j - 1))
            .foreach {
              case (x, y) if (x >= 0 && x <= field.size.x && y >= 0 && y <= field.size.y) => {
                val (ccell, cwhose) = fieldArray(x)(y)

                val newenemy = ccell.clone()
                newenemy(1) = newenemy(1) + 1
                newenemy(cwhose) = newenemy(cwhose) - 1

                currentSum = currentSum +
                  crang((newenemy, cwhose)) -
                  crang((ccell, cwhose))
              }
              case _ =>
            }

          val newenemy = cells.clone()
          newenemy(1) = newenemy(1) + 1
          newenemy(whose) = newenemy(whose) - 1

          currentSum = currentSum +
            crang((newenemy, 1)) -
            crang((cells, whose))

          if( currentSum > max )
          {
            x = i; y = j; max = currentSum
            //println( s"${i}:${j}=${currentSum}")
          }
        }

        j=j+1
      }
      i=i+1
    }

    println( s"${x}:${y}=${fieldSum}->${max}")

    if ( max == 0 ) {
      (0 to field.size.y).foreach {y=>
        (0 to field.size.x).foreach{ x =>
          val t: Turn = this ->(x, y)
          if (t.validate)
            return t
        }
      }
      throw new Exception("Turn can not define")
    }
    else
    {
      val t = (this)->(x,y)
      if( !t.validate )
        println( t )

      t
    }
  }

  var fieldSum:Double = 0
  var playerCount = 2
  var player2Idx=mutable.HashMap.empty[Bot, Int]
  var fieldArray:Array[Array[(ArrayBuffer[Int],Int)]] = null
  lazy val init = {
    fieldArray = Array.fill[(ArrayBuffer[Int],Int)]( field.size.x+1, field.size.y+1 )( ArrayBuffer.fill[Int](12)(0), 0 )
    //fieldArray.foreach { col => col.foreach { cell =>
    //  cell._1(0) = 9
    //}}

    player2Idx.put( null, 0 )
    player2Idx.put( this, 1 )

    crang = rangf
  }

  def sum = {
    fieldArray.foldLeft( 0.0d ){ (sum, i) =>
      sum + (i.foldLeft( 0.0d ){ (sum, j) => sum + crang(j) })
    }
  }

/*
  def rang(i:Int,j:Int):Int = {
    val (own,enemy) = Seq((i - 1, j + 1), (i - 1, j), (i - 1, j - 1), (i, j + 1), (i, j - 1), (i + 1, j + 1), (i + 1, j), (i + 1, j - 1))
      .foreach {
      case (x, y) if (x >= 0 && x < field.size.x && y >= 0 && y < field.size.y) =>
        val cell = field.get( Coord(x,y)).get
        (if (cell.whose.contains(this)) sum._1 + 1 else sum._1,
         if (cell.whose.exists(_ != this)) sum._2 + 1 else sum._2)

    }

    own - enemy
  }
*/

  var crang:((ArrayBuffer[Int],Int))=>Double = null
  def rang:((ArrayBuffer[Int],Int))=>Double = { case (bots, whose) =>

    var ownidx = 1//player2Idx.get(this).get
    var own = bots(1)
    var cells = bots.slice(2, bots.size)

    var enemy = cells.max
    var enemy_count = cells.foldLeft(0.0d){ (sum,count) => sum + (if(count > 0) 1.0d else 0.0d) }
    var enemy_koef = 0.8d / (0.6d + enemy_count)

    if( whose == 1) 3.0d * enemy_koef
    else if (own > 4 && whose > 1 ) 5.1d
    else if (own > 2 && whose > 1 && enemy_count <= 1 ) 4.1d
    else if (own > 2 && whose > 1 ) 2.6d * enemy_count
    else if (whose > 1 && enemy < 2) 0.5d * enemy_koef
    else if (whose > 1 ) 0.0d
    else if (enemy > 2 ) 0.3d * enemy_koef
    else if (own > 1 && enemy > 1 && enemy_count <= 1) 2.2d
    else if (own > 1 && enemy > 1) 2.1d * enemy_count
    else if (own > 1 ) 2.7d * enemy_koef
    else if (enemy > 1) 1.8d * enemy_koef
    else if (own > 0) 2.0d * enemy_koef
    else if (enemy > 0) 1.0d * enemy_koef
    else 1.7d
  }

  def rangf:((ArrayBuffer[Int],Int))=>Double = { case (bots, whose) =>
    var ownidx = 1//player2Idx.get(this).get
    var own = bots(1)
    var cells = bots.slice(2, bots.size)

    var enemy = cells.max
    var enemy_count = cells.foldLeft(0d){(sum,i) => sum + (if(i > 0) 1.0d else 0.0d)}
    //var total = cells.foldLeft(0.0d)(_+_) + 1d

    val a = 1.3
    val b = 0.6
    val b1 = 0.19
    val c = 0.05
    val c1 = 0.12
    val d = .9
    val e = .6

    val res =  a*  bots(0) +
     b* ( if( own >= 2 ) (enemy_count + 1d) else 0 ) +
     b1*own -
     c* ( enemy ) -
     c1*( enemy_count ) +
     d* (if( whose == 1 ) 1 else 0 ) -
     e* (if( whose > 1) 1 else 0 )

    //println(s"""${a*  bots(0)} + ${b*  ( if( own >= 2 ) (enemy_count + 1d) else 0 )} + ${b1* own} - ${c*  ( enemy )} - ${c1*  ( enemy_count )} + ${d* (if( whose == 1 ) 1 else 0 )} - ${e* (if( whose > 1) 1 else 0 )} = ${res} """)
    res
  }
/*
  def merang:((Int,Int,Int))=>Double = { case (own, enemy, whose) =>

    if( whose == 1) 3.0d
    else if (own > 2 && whose == -1 && enemy > 2) 2.2d
    else if (whose == -1 && enemy < 2) 0.4d
    else if (whose <= -1 ) -1.0d
    else if (enemy > 2) 0.3d
    else if (own > 1 && enemy > 1) 1.1d
    else if (own > 1 ) 1.2d
    else if (enemy > 1) 1.0d
    else if (own > 0) 1.8d
    else if (enemy > 0) 0.75d
    else 1.81d
  }
*/
  /*
      field.get(Coord(i, j)) match {
        case Some(cell) =>
          val (own, enemy) = cell.neighbours.foldLeft(0, 0) { (sum, cell) =>
            (if (cell.whose.contains(this)) sum._1 + 1 else sum._1,
              if (cell.whose.exists(_ != this)) sum._2 + 1 else sum._2)
          }

          fieldArray(i)(j) = ((own,enemy))
        case _ => 1 }
    }
  */

/*

  4 4 4 3
  4 x 4
  4 4 4

  4 4 2 C
  4 x 2 2
  4 4 4 3

  2 1 C C C
  4 3 1 0 1
  4 x 4 4 5 6 5
  4 4 4 4 x x x

  x 1 C
  x 1 C
*/

}
