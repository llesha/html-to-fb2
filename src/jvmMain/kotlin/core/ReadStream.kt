package core

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

actual class ReadStream actual constructor(path: String) {
    private val path: String
    private val stream: BufferedReader

    init {
        this.path = path
        stream = BufferedReader(InputStreamReader(FileInputStream(path)))
    }

    actual fun readNChars(n: Int): String {
        val res = StringBuilder()

        repeat(n) {
            val char = stream.read()
            if (char == -1)
                return res.toString()
            res.append(Char(char))
        }

        return res.toString()
    }
}
