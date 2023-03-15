package core

import Constants
import Constants.ignoredTags
import tags.TagFactory

class HtmlTranslator(private val url: String, stream: WriteStream) {
    private val fb2Text = stream
    private val tagFactory = TagFactory()

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
        val (_, element) = XmlParser().parseFromLink(url)
        traverseElement(element)

        return Pair(setOf(), listOf())
    }

    fun traverseElement(element: TraversableElement) {
        if (element.getTag() in Constants.getAddedTags()) {
            fb2Text.add(tagFactory.createTag(element).getFb2(this))
        }

        if (!isTagIgnored(element.getTag()))
            element.getChildren().forEach { traverseElement(it) }
    }
}
