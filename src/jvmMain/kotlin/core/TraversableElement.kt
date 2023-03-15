package core

import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

actual class TraversableElement actual constructor(content: Any?) {
    private val element: Element

    init {
        element = content as Element
    }

    actual fun getChildren(): List<TraversableElement> {
        return element.children().map { TraversableElement(it) }
    }

    actual fun getTag(): String {
        return element.tagName()
    }

    actual fun getAttributes(): Map<String, String> {
        return element.attributes().associate { it.key to it.value }
    }

    actual fun getText(): String {
        val res = XmlBuilder()
        var addedTag = true
        when (element.tagName()) {
            "bold", "b", "strong" -> res.addTag("strong")
            "em", "i" -> res.addTag("emphasis")
            "sub", "sup", "code", "p" -> res.addTag(element.tagName())
            "del" -> res.addTag("strikethrough")
            else -> addedTag = false
        }
        println(res.getResult())
        getChildrenTexts(res)
        if (addedTag)
            res.closeTag()

        return res.toString()
    }

    private fun getChildrenTexts(xmlBuilder: XmlBuilder) {
        element.childNodes().map {
            when (it) {
                is Element -> xmlBuilder.add(TraversableElement(it).getText())
                is TextNode -> xmlBuilder.add(it.text())
                else -> println(it)
            }
        }
    }
}
