package hk.edu.vtc.it.codemonitor

import java.io.File
import java.net.{InetAddress, NetworkInterface, SocketTimeoutException}
import java.nio.file.Path
import java.util.Date

import com.beachape.filemanagement.RegistryTypes.Callback

import scala.io.Source
import scalaj.http.Http
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

class CodeChangeMonitor(sourceDir: File, student: Student) {
  private val startTime = new Date
  private val markerParser = new MarkerParser(sourceDir)

  private def getHashCode(fileContents: String): Int = fileContents.replaceAll("\\s{2,}", "").trim().hashCode

  private val cache = collection.mutable.Map[String, String]()

  private def checkHashCode(path: Path): Option[(Path, String)] = {
    val fileContents = Source.fromFile(path.toString).getLines.mkString("\n")
    val newHashCode = getHashCode(fileContents).toString
    val lastHashcode = cache.getOrElse(path.toString, "")
    if (lastHashcode != newHashCode) {
      cache.put(path.toString, newHashCode.toString)
      println("Code has changed!")
      Option(path, fileContents)
    }
    else {
      println("Code is same!")
      None
    }
  }

  private def getAnswer(t: (Path, String)): Option[(Path, String, List[QuestionAnswer])] = {
    val answer = markerParser.getAnswers(t._1.toFile)
    Option(t._1, t._2, answer)
  }

  case class Metadata(hostname: String, ip: String, mac: String, email: String, filePathName: String, answers: List[QuestionAnswer], startTime: Date)

  private def uploadChanged(t: (Path, String, List[QuestionAnswer])): Option[Boolean] = {
    try {
      println(t)
      val filePathName = t._1.getFileName.toString
      val ip = InetAddress.getLocalHost
      val network = NetworkInterface.getByInetAddress(ip)
      val mac = network.getHardwareAddress.toString
      val metadata = Metadata(ip.getHostName, ip.getHostAddress, mac, student.email, filePathName, t._3, startTime)

      implicit val formats = Serialization.formats(NoTypeHints)

      val body = write(metadata)
      val answers = write(t._3)
      println(answers)
      val response = Http(student.api + "/resource/" + student.email)
        .put(body)
        .param("code", t._2)
        .param("answers", answers)
        .asParamMap
      println(response.code)
      Option(response.code == 200)
    }
    catch {
      case e: SocketTimeoutException => println("Connection Error!"); None;
      case e: Exception => e.printStackTrace(); None;
    }
  }

  val modifyCallbackFile: Callback = { path => {
    println(s"Modified $path")

    val extension = markerParser.extractExt(path.toString)
    if (fileExtensions.contains(extension)) {
      println(s"Process $path")
      val result = checkHashCode(path).flatMap(getAnswer).flatMap(uploadChanged)
      if (result.isEmpty) {
        println("No Upload!")
      } else {
        println("Successful upload is " + result.get)
      }
    }
  }
  }
}
