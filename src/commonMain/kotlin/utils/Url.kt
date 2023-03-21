package utils

typealias Url = String

fun Url.removeProtocol(): Url {
    return this.removePrefix("https://").removePrefix("http://")
}

fun Url.getSite(): Url {
    val urlWithoutProtocol = this.removeProtocol()
    return urlWithoutProtocol.substring(0, urlWithoutProtocol.indexOf("/"))
}

/**
 * Converts https://llesha.github.io/regina/syntax#top-level-declarations
 * to regina/syntax
 */
fun Url.getPageUrl(): Url {
    return this.removeProtocol().removeSite().removeHashFromUrl()
}

fun Url.addSite(): Url {
    val res = StringBuilder(this)
    if (!res.contains("."))
        res.insert(0, Constants.currentSite)

    return res.toString()
}

/**
 * Do it after removing protocol
 */
private fun Url.removeSite(): Url {
    return this.substring(this.indexOf("/") + 1)
}

fun Url.removeHashFromUrl(): Url {
    val indexOfHash = this.indexOf("#")
    return (if (indexOfHash == -1) this else this.substring(0, indexOfHash)).removeSuffix("/").removeSuffix(".html")
}
