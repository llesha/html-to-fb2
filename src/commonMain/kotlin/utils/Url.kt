package utils

typealias Link = String

fun Link.removeProtocol(): Link {
    return this.removePrefix("https://").removePrefix("http://")
}

fun Link.getSite(): Link {
    val urlWithoutProtocol = this.removeProtocol()
    return urlWithoutProtocol.substring(0, urlWithoutProtocol.indexOf("/"))
}

/**
 * Converts https://llesha.github.io/regina/syntax#top-level-declarations
 * to regina/syntax
 */
fun Link.getPageUrl(): Link {
    return this.removeProtocol().removeSite().removeHashFromUrl()
}

fun Link.addSite(): Link {
    val res = StringBuilder(this)
    if (!res.contains("."))
        res.insert(0, Constants.currentSite)

    return res.toString()
}

/**
 * Do it after removing protocol
 */
private fun Link.removeSite(): Link {
    return this.substring(this.indexOf("/") + 1)
}

fun Link.removeHashFromUrl(): Link {
    val indexOfHash = this.indexOf("#")
    return (if (indexOfHash == -1) this else this.substring(0, indexOfHash)).removeSuffix("/").removeSuffix(".html")
}
