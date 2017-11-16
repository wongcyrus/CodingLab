package hk.edu.vtc.it.codemonitor

import java.io.File

import com.google.zxing.client.j2se.BufferedImageLuminanceSource

class QrCode {

  import java.io.{FileNotFoundException, IOException}

  import com.google.zxing.common.HybridBinarizer
  import com.google.zxing.{BinaryBitmap, MultiFormatReader, NotFoundException}

  @throws[FileNotFoundException]
  @throws[IOException]
  @throws[NotFoundException]
  def readQRCode(filePath: String): String = {
    import javax.imageio.ImageIO
    val image = ImageIO.read(new File(filePath))
    val binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)))
    val qrCodeResult = new MultiFormatReader().decode(binaryBitmap)
    qrCodeResult.getText
  }

  def getStudentFromJson(json: String): Student = {
    import com.google.gson.Gson
    new Gson().fromJson(json, classOf[Student])
  }
}
