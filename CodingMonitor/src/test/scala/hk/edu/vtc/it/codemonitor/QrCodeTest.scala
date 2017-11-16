package hk.edu.vtc.it.codemonitor

import scala.io.Source


class QrCodeTest extends org.scalatest.FunSuite {


  test("Decode QR Code") {
    val qrcode = new QrCode
    val json = qrcode.readQRCode(s"$testFolder\\student.png")
    println(json)
    assert(json.contains("cywong@vtc.edu.hk"))
  }

  test("Get Student from Json") {
    val qrcode = new QrCode
    val fileContents = Source.fromFile(s"$testFolder\\student.json").getLines.mkString
    val student = qrcode.getStudentFromJson(fileContents)
    assert(student.email == "cywong@vtc.edu.hk")
  }
}
