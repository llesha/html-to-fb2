package core

import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import utils.addSite
import utils.removeHashFromUrl
import utils.removeProtocol

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

    actual fun getText(): Triple<String, Set<String>, Boolean> {
        val res = XmlBuilder()
        val links = mutableSetOf<String>()
        var addedTag = true
        when (element.tagName()) {
            "bold", "b", "strong" -> res.addTag("strong")
            "em", "i" -> res.addTag("emphasis")
            "sub", "sup", "code", "p" -> res.addTag(element.tagName())
            "del" -> res.addTag("strikethrough")
            "a" -> {
                val linkValue = element.attributes().get("href").removeProtocol().removeHashFromUrl().addSite()
                res.addTagWithAttributes("a", "l:href=\"#$linkValue\"")
                links.add(linkValue)
            }
            else -> addedTag = false
        }
        val (childLinks, hasText) = getChildrenTexts(res)
        links.addAll(childLinks)
        if (addedTag)
            res.closeTag()

        return Triple(res.toString(), links, hasText)
    }

    private fun getChildrenTexts(xmlBuilder: XmlBuilder): Pair<List<String>, Boolean> {
        val links = mutableListOf<String>()
        var hasText = false
        element.childNodes().forEach {
            when (it) {
                is Element -> {
                    val (text, childLinks, childHasText) = TraversableElement(it).getText()
                    if (childHasText)
                        hasText = true
                    xmlBuilder.add(text)
                    links.addAll(childLinks)
                }
                is TextNode -> {
                    val text = it.outerHtml()
                    xmlBuilder.add(text)
                    if (!text.matches(Regex("\\s*")))
                        hasText = true
                }
                else -> println(it)
            }
        }
        return links to hasText
    }
}
