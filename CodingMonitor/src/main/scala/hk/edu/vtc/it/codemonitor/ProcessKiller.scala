package hk.edu.vtc.it.codemonitor

import akka.actor.{Actor, ActorSystem, Cancellable, Props}

import scala.concurrent.duration._
import scala.language.postfixOps

class ProcessKiller(system: ActorSystem) {
  private def killBlackListedProgram(): Unit = {
    println("killBlackListedProgram")
    import scala.sys.process._
    val blacklist = List(
      "chrome.exe",
      "iexplore.exe",
      "firefox.exe",
      "WINWORD.EXE",
      "notepad.exe",
      "Acrobat.exe",
      "AcroRd32.exe"
    )
    val runningTasks = "tasklist.exe".!!.toLowerCase

    val found: Boolean = blacklist.map(_.toLowerCase).foldRight(false)((f, n) => runningTasks.contains(f) || runningTasks.contains(n))
    if (found) {
      println("Need to kill blacklist program")
      blacklist.foreach(a => s"TASKKILL /IM $a /F".!)
      print("\007\007\007\007\007")
    }
  }

  val Tick = "tick"
  private val tickActor = system.actorOf(Props(new Actor {
    def receive: PartialFunction[Any, Unit] = {
      case Tick ⇒ killBlackListedProgram()
    }
  }))
  //Use system's dispatcher as ExecutionContext
  import system.dispatcher

  //This will schedule to send the Tick-message
  //to the tickActor after 0ms repeating every 50ms
  val cancellable: Cancellable =
  system.scheduler.schedule(0 milliseconds,
    10 seconds,
    tickActor,
    Tick)
}
