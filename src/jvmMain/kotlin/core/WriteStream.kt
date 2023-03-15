package core

import Constants
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.nio.charset.Charset

actual class WriteStream actual constructor(path: String) {
    private val path: String
    private val stream: BufferedOutputStream

    init {
        this.path = path
        stream = BufferedOutputStream(FileOutputStream(path))
    }

    actual fun add(text: String) {
        stream.write(text.toByteArray(Charset.forName(Constants.charset)))
        stream.flush()
    }

    actual fun add(stream: ReadStream) {
        do {
            val chars = stream.readNChars(Constants.bufferSize)
            add(chars)
        } while (chars.length == Constants.bufferSize)
    }

    actual fun close() {
        stream.close()
    }

    actual override fun toString(): String {
        TODO("Not yet implemented")
    }
}
