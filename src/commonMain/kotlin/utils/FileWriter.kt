package utils

expect object FileWriter {
    fun write(content: String, path: String)
}