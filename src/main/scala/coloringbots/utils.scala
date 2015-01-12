package coloringbots

import org.aop4scala._
import org.aspectj.weaver.tools.PointcutExpression

import scala.collection.mutable
import scala.util.Try

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 25.12.14
 * Time: 10:57
 *
 */

/* Данный объект потребовался для поддержки синтаксиса, используетмого в объекте Round */
case class TurnMaker(bot: Option[Bot], cell: Option[Cell]) {
  /**
   * Получаем ход, валидируем его и выполняем и возвращаем пустой TurnMaker.
   * Если была ошибка - возвращаем TurnMaker с установленым ботом  */
   def paint(turn: (Bot)=> Turn): TurnMaker = this take turn map validate map perform getOrElse bad

  /* Если результатом paint возвращен TurnMaker с ботом - выполняем alternative, иначе ничего не делаем  */
  def or(alternative: Bot => Unit):   TurnMaker = { bot  foreach alternative; this }
  private def take(turn: (Bot)=> Turn): Try[Turn] = Try(bot map turn get)

  def send(optional: (Cell) => Unit): TurnMaker = { cell foreach optional;    this}
  private def validate(turn: Turn): Turn = if (turn.validate) turn else exception
  private def perform(turn: Turn): TurnMaker = {
    val cell: CellImpl = turn.cell.asInstanceOf[CellImpl]
    cell.set(turn.bot)
    TurnMaker(None, Some(cell))
  }
  private def bad: TurnMaker = TurnMaker(bot, None)
  private def exception = throw new IllegalStateException("Некорректный ход у бота " + bot)
}

abstract trait TimeingInterceptor extends Interceptor
{
  var time:mutable.HashMap[Bot, Long] = mutable.HashMap.empty[Bot, Long]

  abstract override def invoke(invocation: Invocation): AnyRef = {
    if (invocation.target.isInstanceOf[Bot] )
    {
      var startTime = System.nanoTime()
      val result = super.invoke(invocation)
      var endTime = System.nanoTime()

      val bot = invocation.target.asInstanceOf[Bot]
      time.put( bot, time.getOrElse(bot,0L) + (endTime - startTime))
      //println( s"${bot} -> ${invocation.method.getName}:(${format(endTime - startTime)})")

      result
    } else
      super.invoke(invocation)
  }

  override def toString: String = "Aspect Time:\n" + time.map{case(bot, time) => (bot, format(time))}.toString
  private def format(time: Long): String = s"${time/1e6}  ms"
}

/**
 * Список ботов. Если в ходе выполнения бот совершает недопустимое действие, он дисквалифицируется
 */
class Bots{
  private val bots = new mutable.HashSet[Bot]
  private val losers = new mutable.HashSet[Bot]

  private def isActive(bot: Bot) = !(losers contains bot)
  def isFinish() = (bots -- losers).size == 1

  val aspect = new Aspect("execution(* * (..))")
    with TimeingInterceptor

  def register(bot: Bot): Bots = { bots += aspect.create[Bot](bot); this }
  def disqualify(bot: Bot) = { losers += bot }

  def players: Seq[Bot] = bots filter isActive toSeq
  def all: Seq[Bot] = bots toSeq
  def dead: Seq[Bot] = losers toSeq
  def foreach(turn: (Bot) => Unit) = players foreach turn
  def forall(turn: (Bot) => Unit)  = all foreach turn

  override def toString: String = aspect.toString
}

class Timer{
  private val map = new mutable.HashMap[Bot, Long]()

  def action(bot: Bot, f: Bot=>Unit) = {
    val start = System.nanoTime
    f(bot)
    val end = System.nanoTime
    map.put( bot, map.getOrElse(bot, 0L) + end - start)

    //println( s"${bot} -> action (${format(end - start)})")
  }

  override def toString: String = "Timer: " + map.map{case(bot, time) => (bot, format(time))}.toString

  private def format(time: Long): String = s"${time/1e6}  ms"
}

/* Данный объект создан для поддержки синтаксиса, используемого в методе CellImpl.neighbours */
case class Neighbours(private val set: Set[Cell]){
  def +(f: => Cell): Neighbours = Try(f).toOption.fold(this){c =>Neighbours(set + c)}
  def toSet = set.toSet
}

/* неявные преобразования */
object Utils {
//  implicit def bots2Round(bots: Bots): Round = new Round(bots)
  implicit def bot2TurnMaker(bot: Bot):TurnMaker = TurnMaker(Some(bot), None)
  implicit def option2Cell(o: Option[Cell]): Cell = o.get
  implicit def neighbours2Set(n: Neighbours): Set[Cell] = n.toSet
  implicit def tuple2Coord(t: (Int, Int)): Coord = Coord(t._1, t._2)
  def N = Neighbours(Set())
}

