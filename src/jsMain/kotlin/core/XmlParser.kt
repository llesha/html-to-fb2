package core

import core.MetaInformation
import core.TraversableElement
import org.w3c.dom.parsing.DOMParser

actual class XmlParser {
    actual fun parse(text: String): Pair<MetaInformation, TraversableElement> {
        val parser = DOMParser()
        val doc = parser.parseFromString(text, "text/xml");
        println(doc)
        return MetaInformation(TraversableElement()) to TraversableElement()
    }

    actual fun parseFromLink(url: String): Pair<MetaInformation, TraversableElement> {
        TODO("Not yet implemented")
    }
}

actual class TraversableElement actual constructor(content: Any?) {
    actual fun getChildren(): List<TraversableElement> {
        return listOf()
    }

    actual fun getTag(): String {
        return ""
    }

    actual fun getText(): String {
        TODO("Not yet implemented")
    }

    actual fun getAttributes(): Map<String, String> {
        TODO("Not yet implemented")
    }
}