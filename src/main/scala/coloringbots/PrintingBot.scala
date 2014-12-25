package coloringbots

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 25.12.14
 * Time: 18:20
 * Since: 
 *
 */
class PrintingBot extends Bot
{
  override def notify(cell: Cell): Unit = this print field
  override def nextTurn: Turn = throw new UnsupportedOperationException()
  override def color: String = "None"
  override var field: Field = null

  def print(field: Field) = {

  }

}
