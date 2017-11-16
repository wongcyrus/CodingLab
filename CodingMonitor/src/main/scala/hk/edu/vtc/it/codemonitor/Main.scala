package hk.edu.vtc.it.codemonitor

import java.io.File
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY

import akka.actor.ActorSystem
import com.beachape.filemanagement.Messages.RegisterCallback
import com.beachape.filemanagement.MonitorActor

object Main extends App {
  val jarLocation = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath.substring(1).replace("monitor.jar", "")
  println(jarLocation)
  val jarFolder = "C:\\Users\\developer\\Downloads"

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  implicit val system: ActorSystem = ActorSystem("actorSystem")
  val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))

  val qrcodeFile = s"$jarFolder\\student.png"
  val qrcode = new QrCode
  val student = qrcode.getStudentFromJson(qrcode.readQRCode(qrcodeFile))
  val labSource = new LabSource(student.source)
  val source = labSource.sourceDir
  println("You Lab Source " + source)
  val codeChangeMonitor = new CodeChangeMonitor(source, student)

  recursiveListFiles(source).foreach(f => {
    if (f.isDirectory) {
      fileMonitorActor ! RegisterCallback(
        event = ENTRY_MODIFY,
        path = f.toPath,
        callback = codeChangeMonitor.modifyCallbackFile
      )
    }
  })

  if (student.isTest)
    new ProcessKiller(system)

}
