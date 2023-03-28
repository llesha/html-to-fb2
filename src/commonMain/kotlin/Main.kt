fun main(args: Array<String>) {
    var url: String? = null
    var path = "result.fb2"
    args.toList().zipWithNext { name, value ->
        when (name) {
            "-u" -> url = value
            "-p" -> path = value
            "-l" -> Constants.linkLevel = value.toInt()
            "-i" -> Constants.withImages = getBoolean(name, value)
            "-c" -> Constants.charset = value
            "-b" -> Constants.bufferSize = value.toInt()
            "-t" -> Constants.isTree = getBoolean(name, value)
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
    -l [-1] - add links in fb2 in this domain.
        -1 to add all found links, 
        0 to add only starting page, 
        n > 0 to add all pages reachable from starting with not more than n link clicks
    -t true/[false] - structure document like a tree with urls. Setting to `true` will make it run ~2 times slower, but guarantees section sorting
    -i true/[false] - add images to fb2
    -c [UTF-8] - used charset
    -b [1000] - stream buffer size. Bigger for faster conversion, smaller if not enough memory"""
    )
}
