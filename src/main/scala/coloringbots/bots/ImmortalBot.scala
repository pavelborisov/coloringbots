package coloringbots.bots

import coloringbots._

/**
 * Created by pavel on 30.12.14.
 */
case class ImmortalBot(val color: String) extends Bot
{
  implicit def cellOpt(opt:Option[Cell]) = opt.get
  override def nextTurn: Turn = {

    for(i <- -1 to field.size.y - 2) {
      for(j <- -1 to field.size.x - 2) {
        if( validateStart( j, i ) ) {
          val move = this ->(j + 1, i + 1)
          if( move.validate )
            return move
        }
      }
    }

    for(r <- 2 to 1 by -1 ) {
      for (i <- 0 to field.size.y - 1) {
        for (j <- 0 to field.size.x - 1) {
          field.get(Coord(j, i)).whose match {
            case Some(bot) if bot == this =>
              val turn = findInRadius(j, i, r)
              if (turn.isDefined)
                return turn.get
            case _ =>
          }
        }
      }
    }

    //throw new Exception("Turn can not define")


    def validateStart( startX:Int, startY:Int):Boolean = {
      (0 to 1).map(_ * 2 + startY).filter { y => y >= 0 && y < field.size.y }.find { y =>
        ((0 to 1).map(_ * 2 + startX).filter { x => x >= 0 && x < field.size.x}.find { x =>
            val t: Turn = this ->(x, y)
            !(t.validate)
          }) != None
      } == None
    }

    def findInRadius(centerX:Int, centerY:Int, radius:Int):Option[Turn] = {

      List( (-radius + 1 to radius - 1).map(_ + centerY).map{ y => List( (centerX - radius, y), (centerX + radius, y) )}.flatten ,
        (-radius + 1 to radius - 1).map(_ + centerX).map{ x => List( (x, centerY - radius), (x, centerY + radius) )}.flatten )
        .flatten.find{ case(x,y) =>
          { x >= 0 && x < field.size.x } &&
          { y >= 0 && y < field.size.y } &&
          (this->(x,y)).validate }.map { case (x,y) => (this->(x,y)) }
/*        .orElse(
        (-radius + 1 to radius - 1).map(_ + centerX).map{ x => List( (x, centerY - radius), (x, centerY + radius) )}
          .flatten.find{ case(x,y) =>
          { x >= 0 && x < field.size.x } &&
          { y >= 0 && y < field.size.y } &&
          (this->(x,y)).validate }.map { case (x,y) => (this->(x,y)) })*/
    }

    this ->(0, 0)
  }

  override def notify(cell: Cell) = {}
  override var field: Field = null
}
