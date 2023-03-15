package core

expect class WriteStream(path: String) {
    fun add(text: String)
    fun add(stream: ReadStream)
    fun close()

    override fun toString(): String
}

expect class ReadStream(path: String) {
    fun readNChars(n: Int): String
}