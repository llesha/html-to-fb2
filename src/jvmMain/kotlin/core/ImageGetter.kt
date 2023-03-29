package core

import java.io.File
import java.net.URL
import java.util.*

actual class ImageGetter actual constructor() {
    actual fun getImage(link: String) {
        val javaUrl = URL(link)
        val inputStream = javaUrl.openStream()
        val path = "..\\binaries"
        File(path).createNewFile()

        val outputStream = WriteStream(path)
        val byteArray = ByteArray(2048)
        var length: Int

        while (inputStream.read(byteArray).also { length = it } != -1) {
            outputStream.add(Base64.getEncoder().encodeToString(byteArray))
        }

        inputStream.close()
        outputStream.close()
    }
}
