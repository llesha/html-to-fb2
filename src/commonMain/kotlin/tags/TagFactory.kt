package tags

import core.TraversableElement

class TagFactory {
    fun createTag(element: TraversableElement): Tag {
        return when (element.getTag()) {
            "image" -> Image(element)
            else -> Text(element)
        }
    }
}