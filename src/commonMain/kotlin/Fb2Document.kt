import core.*
import utils.Date

class Fb2Document(private val indexUrl: String) {
    private val currentText = WriteStream("C:\\Users\\alex\\Desktop\\book.fb2")

    private val links = mutableSetOf<String>()
    private val binaries = mutableListOf<ByteArray>()

    fun create() {
        currentText.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        currentText.add(
            XmlBuilder().addTagWithAttributes(
                "FictionBook",
                "xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\"",
                "xmlns:l=\"http://www.w3.org/1999/xlink\""
            ).getResult()
        )
        createDescription(XmlParser().parseFromLink(indexUrl).first)
        traverseHtml(indexUrl)
        addBinaries()

        currentText.add("</FictionBook>")
        currentText.close()
    }

    private fun traverseHtml(url: String) {
        val bodyBuilder = XmlBuilder().addTag("body")
        currentText.add(bodyBuilder.getResult())

        val (links, binaries) = HtmlTranslator(url, currentText).translateToFb2()

        currentText.add(bodyBuilder.clearWithoutTags().closeTag().getResult())
        this.links.addAll(links)
        this.binaries.addAll(binaries)
    }

    private fun createDescription(meta: MetaInformation) {
        val xmlBuilder = XmlBuilder()
        xmlBuilder.addTag("description") {
            xmlBuilder.addTag("title-info") {
                xmlBuilder.addTag("title", meta.title ?: indexUrl.substring(indexUrl.lastIndexOf("/") + 1))
                xmlBuilder.addTag("author", meta.author ?: "unknown author")
                xmlBuilder.addTag("genre", "blog")
                xmlBuilder.addTag("lang", meta.language ?: Constants.language)
                if (meta.description != null)
                    xmlBuilder.addTag("annotation", meta.description)
            }

            xmlBuilder.addTag("document-info") {
                xmlBuilder.addTag("date", meta.findValue("date") ?: Date.today())
                xmlBuilder.addTag("author", meta.author ?: "unknown author")
                xmlBuilder.addTag("id", meta.findValue("id") ?: "1")
                xmlBuilder.addTag("version", meta.findValue("version") ?: "1.0")
            }
        }

        currentText.add(xmlBuilder.getResult())
    }

    private fun addBody(body: StringBuilder, binaries: List<ByteArray>) {
        currentText.add(XmlBuilder().addTag("body", body.toString()).getResult())
        this.binaries.addAll(binaries)
    }

    private fun addBinaries() {}
}