package core

import Constants
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import utils.removeHashFromUrl
import utils.removeProtocol

actual open class TraversableElement actual constructor(content: Any?) {
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
                val linkValue = getLinkFromATag(element)
                if (!linkValue.contains(".")) {
                    res.addTagWithAttributes(
                        "a", "l:href=\"#${
                            linkValue
                                .removePrefix(Constants.currentSite)
                                .replace("\"", "&quot;")
                        }\""
                    )
                    links.add(linkValue)
                } else res.addTagWithAttributes("a", "l:href=\"${linkValue.replace("\"", "&quot;")}\"")
            }
            else -> addedTag = false
        }
        val (childLinks, hasText) = getChildrenTexts(res)
        links.addAll(childLinks)
        if (addedTag)
            res.closeTag()

        return Triple(res.toString(), links, hasText)
    }

    private fun getLinkFromATag(element: Element): String {
        return element.attributes().get("href").removeProtocol().removeHashFromUrl()
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

    actual fun getLinks(): MutableSet<String> {
        val linkSet = mutableSetOf<String>()
        if (element.tagName() == "a") {
            val linkValue = getLinkFromATag(element)
            if (!linkValue.contains("."))
                linkSet.add(linkValue)
        }
        for (e in this.element.children())
            linkSet.addAll(TraversableElement(e).getLinks())

        return linkSet
    }
}
