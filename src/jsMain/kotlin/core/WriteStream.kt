package core

import org.w3c.files.Blob

actual class WriteStream actual constructor(path: String) {
    val str = Blob()
    actual fun add(text: String) {}

    actual fun add(stream: ReadStream) {}

    actual override fun toString(): String {
        TODO("Not yet implemented")
    }

    actual fun close() {}
}

actual class ReadStream actual constructor(path: String) {
    actual fun readNChars(n: Int): String {
        TODO("Not yet implemented")
    }
}