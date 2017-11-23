package hk.edu.vtc.it.codemonitor

import java.io.File

import org.scalatest.FunSuite

class CodeChangeMonitorTest extends FunSuite {

  test("Code Change Monitor") {
    val source = new File(s"$testFolder\\Lab01_student\\")
    val student = Student("source", "api", "email", "apikey", false)
   new CodeChangeMonitor(source, student)
    //assert(fileSize > 0)
  }
}
