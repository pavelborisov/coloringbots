package coloringbots

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:28
 * Since: 
 *
 */
/**
 * Объект Координаты (x,y)
 */
case class Coord(x: Int, y: Int) {
  override def toString = s"{$x,$y}"
  override def hashCode(): Int = x.hashCode() * 1000 + y.hashCode()
  override def equals(obj: scala.Any): Boolean = obj.isInstanceOf[Coord] && x == obj.asInstanceOf[Coord].x && y == obj.asInstanceOf[Coord].y
}

/**
 * трейт Ячейка: координаты, соседние ячейки, бот закрасивший ячейку
 */
trait Cell {
  /* Координаты ячейки на поле */
  def coord: Coord
  /* Поле */
  def field: Field
  /* Бот */
  def whose: Option[Bot]
  /* соседи */
  def neighbours: Set[Cell]
  /* Ячейка сверху */
  def up: Option[Cell]
  /* Ячейка снизу */
  def down: Option[Cell]
  /* Ячейка слева */
  def left: Option[Cell]
  /* Ячейка вправа */
  def right: Option[Cell]

  def isEmpty: Boolean = whose.isEmpty
}

/* Поле */
trait Field {
  /* Размер поля (координаты крайней ячейки) */
  def size: Coord
  /* Возвращает ячейку на поле по координате coord */
  def get(coord: Coord): Option[Cell]
  /* Матрица всех ячеек */
  def cells: Array[Array[Cell]]
}

/* Бот */
trait Bot{
  /* Поле. Устанавливается при регистрации бота в игре */
  var field: Field
  /* Цвет, которым закрашивает бот ячейки на поле */
  def color: String
  /* Возвращает следующий ход */
  def nextTurn: Turn
  /* Оповещение о закрашивании ячейки cell */
  def notify(cell: Cell): Unit

  protected def ->(cell: Cell): Turn = Turn(this, cell)
}

/* объект Ход, определяется намерениями бота @param bot закрасить ячейку cell */
case class Turn(bot: Bot, cell: Cell){
  /* Определяет корректность данного хода */
  def validate: Boolean = canPaint
  def canPaint = isBlank || canFill || canOver

  /* ячейка пуста */
  protected def isBlank: Boolean = cell.isEmpty && cell.neighbours.forall(_.whose.isEmpty)
  /* ячейка пуста и есть как минимум 2х соседних своих ячеек */
  protected def canFill: Boolean = isBlank && cell.neighbours.count( _.whose.contains(bot)) > 1
  /* есть как минимум 3х соседних своих ячеек  */
  protected def canOver: Boolean = cell.neighbours.count( _.whose.contains(bot)) > 2
}

