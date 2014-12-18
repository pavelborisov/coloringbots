/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:28
 * Since: 
 *
 */

class Coord(val x: Int, val y: Int) {
  override def toString = s"{$x,$y}"
}

trait Cell {
  def coord: Coord
  def set(bot: Bot): Option[Bot]
  def get: Option[Bot]
}

trait Field {
  def size: Coord
  def get(coord: Coord): Option[Cell]
  def cells: Seq[Cell]
}

trait Bot{
  var field: Field
  def color: String
  def action: Option[Coord]
}

