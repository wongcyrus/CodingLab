name := "CodingMonitor"

version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.beachape.filemanagement" %% "schwatcher" % "0.3.+",
  "org.apache.httpcomponents" % "httpclient" % "4.5.+",
  "org.scalaj" %% "scalaj-http" % "2.3.+",
  "com.google.code.gson" % "gson" % "2.+",
  "org.zeroturnaround" % "zt-zip" % "1.+",
  "com.google.zxing" % "core" % "3.+",
  "com.google.zxing" % "javase" % "3.+",
  "org.scalatest" %% "scalatest" % "3.+" % "test"
)



ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

assemblyJarName in assembly := "monitor.jar"
mainClass in assembly := Some("hk.edu.vtc.it.codemonitor")
assemblyMergeStrategy in assembly := {
  x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}