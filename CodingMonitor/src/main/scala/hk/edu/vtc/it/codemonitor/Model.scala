package hk.edu.vtc.it.codemonitor

case class Student(source: String, api: String, email: String, apiKey: String, isTest: Boolean)

case class Question(source: String, subQuestion: Set[String])

case class QuestionTag(subQuestion: String, startTag: String, endTag: String)

case class SubQuestionAnswer(name: String, answer: String, error: Boolean)

case class QuestionAnswer(source: String, subQuestion: Set[SubQuestionAnswer])



