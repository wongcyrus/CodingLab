package hk.edu.vtc.it.codemonitor

import org.scalatest.FunSuite

class LabSourceTest extends FunSuite {

  test("Download File") {
    val zipUrl = "https://s3.amazonaws.com/lexinterceptor-voicebucket-19jv3apmz8td9/Lab01_student.zip"
    val labSource = new LabSource(zipUrl)

    val someFile = labSource.zipFile
    val fileSize = someFile.length
    assert(fileSize > 0)
  }

}
