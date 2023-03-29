package core

import org.w3c.dom.parsing.DOMParser

actual class XmlParser {
    actual fun parse(text: String): Pair<MetaInformation, TraversableElement> {
        val parser = DOMParser()
        val doc = parser.parseFromString(text, "text/xml")
        println(doc)
        return MetaInformation(TraversableElement()) to TraversableElement()
    }

    actual fun parseFromLink(link: String): Pair<MetaInformation, TraversableElement>? {
        TODO("Not yet implemented")
    }
}
