package core

import org.jsoup.Jsoup

actual class XmlParser {
    actual fun parse(text: String): Pair<MetaInformation, TraversableElement> {
        val html = Jsoup.parse(text)
        return MetaInformation(TraversableElement(html.head())) to TraversableElement(html.body())
    }

    actual fun parseFromLink(url: String): Pair<MetaInformation, TraversableElement> {
        val html = Jsoup.connect(url).get()
        return MetaInformation(TraversableElement(html.head())) to TraversableElement(html.body())
    }
}
