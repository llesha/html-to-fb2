package core

import Constants
import Constants.ignoredTags
import tags.TagFactory
import utils.getPageUrl

class HtmlTranslator(private val url: String, stream: WriteStream) {
    private val fb2Text = stream
    private val tagFactory = TagFactory()
    private val collectedLinks = mutableSetOf<String>()

    fun isTagAdded(tag: String): Boolean {
        return Constants.getAddedTags().contains(tag)
    }

    fun isTagIgnored(tag: String): Boolean {
        return ignoredTags.contains(tag)
    }

    fun translateToFb2(
        withLinks: Boolean = true,
        withImages: Boolean = false
    ): Pair<Set<String>, List<ByteArray>> {
        val (_, element) = XmlParser().parseFromLink(url) ?: return emptySet<String>() to listOf()
        fb2Text.add(
            XmlBuilder().addTag("title").addTag("p", url.getPageUrl()).closeTag().getResult()
        )
        traverseElement(element)

        return collectedLinks to listOf()
    }

    fun getLinks(): Set<String> {
        val (_, element) = XmlParser().parseFromLink(url) ?: return emptySet()
        val res = mutableSetOf("")
        res.addAll(element.getLinks())
        return res
    }

    fun traverseElement(element: TraversableElement) {
        if (element.getTag() in Constants.getAddedTags()) {
            fb2Text.add(tagFactory.createTag(element).getFb2(this))
        } else if (!isTagIgnored(element.getTag()))
            element.getChildren().forEach { traverseElement(it) }
    }

    fun addLinks(links: Set<String>) {
        collectedLinks.addAll(links)
    }
}
