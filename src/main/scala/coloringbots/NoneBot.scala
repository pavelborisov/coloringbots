package coloringbots

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:37
 * Since: 
 *
 */
class NoneBot extends Bot{
//  override def action: Option[] = Option(new Coord (1,2))


  override def nextTurn = this -> field.get(Coord (1,2)).get

  override def notify(cell: Cell) = {}

  override def color: String = "None"

  override var field: Field = null
}
