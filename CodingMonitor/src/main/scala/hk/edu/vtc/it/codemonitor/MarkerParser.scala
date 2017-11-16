package hk.edu.vtc.it.codemonitor

import java.io.File

import scala.io.Source

class MarkerParser(sourceDir: File) {
  def fileStream(dir: File): Stream[File] =
    if (dir.isDirectory)
      Option(dir.listFiles)
        .map(_.toList.sortBy(_.getName).toStream.flatMap(file => file #:: fileStream(file)))
        .getOrElse {
          println("exception: dir cannot be listed: " + dir.getPath)
          Stream.empty
        }
    else Stream.empty

  val files: Stream[File] = fileStream(sourceDir)

  private def getFileContents(f: File): String = Source.fromFile(f).getLines.mkString

  private val ext = """\.[A-Za-z0-9]+$""".r

  def extractExt(url: String): String = ext findFirstIn url getOrElse ""


  private val questions = for {
    f <- files if !f.isDirectory && fileExtensions.contains(extractExt(f.getName.toLowerCase))
    segment <- Option(getFileContents(f).split("#####")) if segment.length > 1
    q <- Option(segment.filter(f => f.contains("-start####")).map(s => s.split("-start####").head).toSet)
  } yield Question(f.getName, q)

  val questionMap: Map[String, Question] = questions.map(a => a.source -> a).toMap

  val questionTags: Map[String, Set[(String, QuestionTag)]] = questions.map(q => (q.source, q.subQuestion.map(s => q.source -> QuestionTag(s, s"#####$s-start####", s"#####$s-end####")))).toMap

  def getAnswers(file: File): List[QuestionAnswer] = {
    val questionTagOption: Option[Set[(String, QuestionTag)]] = questionTags.get(file.getName)
    val content = getFileContents(file)

    def extractAnswer(s: (String, QuestionTag)) = try {
      Option(content.substring(content.indexOf(s._2.startTag) + s._2.startTag.length, content.indexOf(s._2.endTag)))
    } catch {
      case _: Throwable =>
        println(s"Don't delete $s._2.startTag or s._2.endTag, else you will lose mark!")
        println("\007\007\007\007\007")
        None
    }

    (for {
      fileQuestions <- questionTagOption
      subQuestion <- Option(fileQuestions.map(s => SubQuestionAnswer(s._2.subQuestion, extractAnswer(s).getOrElse(""), extractAnswer(s).isEmpty)))
    } yield QuestionAnswer(file.getName, subQuestion)).toList
  }

}
