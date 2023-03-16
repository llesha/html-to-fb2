fun main(args: Array<String>) {
    var url: String? = null
    var path = "result.fb2"
    args.toList().zipWithNext { name, value ->
        when (name) {
            "-u" -> url = value
            "-p" -> path = value
            "-l" -> Constants.withLinks = getBoolean(name, value)
            "-i" -> Constants.withImages = getBoolean(name, value)
            "-c" -> Constants.charset = value
            "-b" -> Constants.bufferSize = value.toInt()
            else -> {
                printHelp()
                return
            }
        }
    }
    if (url == null) {
        printHelp()
        return
    }
    Fb2Document(url!!, path).create()
}

private fun getBoolean(name: String, value: String): Boolean {
    if (value == "false")
        return false
    if (value == "true")
        return true
    throw IllegalArgumentException("Wrong value for $name argument. `true` or `false` is accepted")
}

private fun printHelp() {
    println(
        """This jar will create fb2 file from html page(s).
Arguments are listed in format <NAME> <VALUE>
Arguments:
    -u - url of converted page
    -p [result.fb2] - path to generated fb2
    -l [true]/false - add links in fb2 in this domain. `false` to convert only current page
    -i true/[false] - add images to fb2
    -c [UTF-8] - used charset
    -b [1000] - stream buffer size. Bigger for faster conversion, smaller if not enough memory."""
    )
}
