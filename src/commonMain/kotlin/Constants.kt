object Constants {
    val textTags = setOf("p", "strong", "b", "i", "em", "a")
    private val addedTags = setOf<String>()
    val ignoredTags = setOf("button")
    var charset = "UTF-8"
    var bufferSize = 1000
    var language = "en"
    var linkLevel = -1
    var withImages = true
    var currentSite = ""
    var isTree = false

    fun getAddedTags(): Set<String> {
        val res = (textTags + addedTags).toMutableSet()
        if (withImages)
            res.add("image")
        return res
    }
}
