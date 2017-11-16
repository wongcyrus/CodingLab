package hk.edu.vtc.it.codemonitor

import java.io.File
import java.nio.file.Paths

import org.scalatest.FunSuite

class MarkerParserTest extends FunSuite {

  test("Test Files List") {
    val folder = Paths.get(testFolder).toFile
    val markerParser = new MarkerParser(folder)
    val files = markerParser.files.toList
    assert(files.nonEmpty)
  }


  test("Test Files sub question") {
    val folder = Paths.get(testFolder).toFile
    val markerParser = new MarkerParser(folder)

    assert(markerParser.questionMap.exists(_._1 == "ex1_1a.php"))
    assert(markerParser.questionMap.find(_._1== "ex1_1a.php").get._2.subQuestion.size == 3)
  }

  test("Test Files extract Answer") {
    val folder = Paths.get(testFolder).toFile
    val markerParser = new MarkerParser(folder)

    val answer = markerParser.getAnswers(new File(s"$testFolder\\Lab01_student\\ex1_1a.php"))
    answer.foreach(println)
    assert(answer.find(_.source == "ex1_1a.php").size == 1)
    assert(answer.find(_.source == "ex1_1a.php").get.subQuestion.find(_.name == "ex1_1a2").get.answer == "*/AnswerTwo/*")
  }

  test("Test Files extract Answer Error") {
    val folder = Paths.get(testFolder).toFile
    val markerParser = new MarkerParser(folder)

    val answer = markerParser.getAnswers(new File(s"$testFolder\\Lab01_student_error\\ex1_1a.php"))
    answer.foreach(println)
    assert(answer.find(_.source == "ex1_1a.php").size == 1)
    assert(answer.find(_.source == "ex1_1a.php").get.subQuestion.find(_.name == "ex1_1a2").get.error)
  }

}
