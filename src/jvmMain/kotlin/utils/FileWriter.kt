package utils

import java.io.File

actual object FileWriter {
    actual fun write(content: String, path: String) {
        File(path).writeText(content)
    }
}