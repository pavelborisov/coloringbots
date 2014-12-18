/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:37
 * Since: 
 *
 */
class NoneBot extends Bot{
  override def action: Option[Coord] = Option(new Coord (1,2))

  override def color: String = "None"

  override var field: Field = null
}
