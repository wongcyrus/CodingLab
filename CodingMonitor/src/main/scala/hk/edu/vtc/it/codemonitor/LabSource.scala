package hk.edu.vtc.it.codemonitor

class LabSource(sourceUrl: String) {

  import java.io.File
  import java.net.URL
  import java.nio.file.{Files, Paths}
  import javax.swing.JOptionPane

  import scala.language.postfixOps
  import sys.process._

  private val sourceDrive = windowDrives.find(d => Files.exists(Paths.get(d)))

  if (sourceDrive.isEmpty) {
    JOptionPane.showMessageDialog(null,
      "This computer don't have d: or e:",
      "Not Support",
      JOptionPane.WARNING_MESSAGE)
    System.exit(0)
  }

  val zipFile: File = Paths.get(sourceDrive.get, "lab.zip").toFile
  new URL(sourceUrl) #> zipFile !!
  val sourceDir: File = Paths.get(sourceDrive.get, "LabSource").toFile


  private def unzip(): Unit = {
    import java.io.{FileInputStream, FileOutputStream}
    import java.util.zip.ZipInputStream

    val buffer = new Array[Byte](1024)
    val zis = new ZipInputStream(new FileInputStream(zipFile))
    var zipEntry = zis.getNextEntry
    while (zipEntry != null) {
      if (!zipEntry.isDirectory) {
        val fileName = zipEntry.getName
        val relativePath = zipEntry.getName.substring(0, zipEntry.getName.lastIndexOf("/"))
        Paths.get(sourceDrive.get, "LabSource", relativePath).toFile.mkdirs
        val newFile = Paths.get(sourceDrive.get, "LabSource", fileName).toFile
        if(!newFile.exists){
          val fos = new FileOutputStream(newFile)
          var len = 0
          do {
            len = zis.read(buffer)
            if (len > 0)
              fos.write(buffer, 0, len)
          } while (len > 0)
          fos.close()
        }
      }
      zipEntry = zis.getNextEntry
    }
    zis.closeEntry()
  }

  unzip()
}
