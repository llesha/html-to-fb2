package core

import org.jsoup.Jsoup
import java.net.SocketTimeoutException

actual class XmlParser {
    actual fun parse(text: String): Pair<MetaInformation, TraversableElement> {
        val html = Jsoup.parse(text)
        return MetaInformation(TraversableElement(html.head())) to TraversableElement(html.body())
    }

    actual fun parseFromLink(link: String): Pair<MetaInformation, TraversableElement>? {
        var timeout = 0
        while (timeout < 5) {
            try {
                val html = Jsoup.connect(link).get()
                return MetaInformation(TraversableElement(html.head())) to TraversableElement(html.body())
            } catch (_: SocketTimeoutException) {
                println("Read timeout: $link")
                timeout++
            } catch (e: Exception) {
                if (e.toString().contains("Status=404")) {
                    println("$link was deleted")
                    return null
                }
                throw e
            }
        }
        throw SocketTimeoutException("Read time out")
    }
}
