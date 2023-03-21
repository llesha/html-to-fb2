import core.*
import utils.Date
import utils.Url
import utils.getPageUrl
import utils.linkSetToTree

class Fb2Document(private val indexUrl: Url, path: String) {
    init {
        setCurrentSite(indexUrl)
    }

    private val currentText = WriteStream(path)

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
        traverseHtml(indexUrl, 0)
        addBinaries()

        currentText.add("</FictionBook>")
        currentText.close()
    }

    private fun setCurrentSite(path: String) {
        val trimmed = path.removePrefix("https://").removePrefix("http://")
        val indexOfSlash = trimmed.indexOf("/")
        Constants.currentSite =
            "https://" + (if (indexOfSlash == -1) trimmed else trimmed.substring(0, indexOfSlash)) + "/"
    }

    private fun traverseHtml(url: Url, level: Int) {
        val bodyBuilder = XmlBuilder().addTag("body").addTag("section")
        currentText.add(bodyBuilder.getResult())

        val (htmlLinks, binaries) = HtmlTranslator(url, currentText).translateToFb2()

        currentText.add(bodyBuilder.clearWithoutTags().closeTag().getResult())
        if (level < Constants.linkLevel)
            links.addAll(htmlLinks)
        this.binaries.addAll(binaries)
        links.addAll(htmlLinks)
        val tree = linkSetToTree(links)
        println(this.links.joinToString(separator = "\n"))

//        for (url in htmlLinks) {
//            bodyBuilder.clearAll().addTagWithAttributes("section", "id=\"${url.getPageUrl()}\"")
//            currentText.add(bodyBuilder.getResult())
//            HtmlTranslator(url, currentText).translateToFb2()
//            currentText.add(bodyBuilder.clearWithoutTags().closeTag().getResult())
//        }
        currentText.add("<image l:href=\"#img1\"></body>")
        ImageGetter().getImage("https://www.yegor256.com/images/face-256x256.jpg")
        currentText.add(XmlBuilder().addTagWithAttributes("binary", "content-type=\"image/jpeg\"",
        "id=\"img1\"").getResult())
        currentText.add(ReadStream("..\\binaries"))
        currentText.add("</binary>")
    }

    private fun createDescription(meta: MetaInformation) {
        val xmlBuilder = XmlBuilder()
        xmlBuilder.addTag("description") {
            xmlBuilder.addTag("title-info") {
                xmlBuilder.addTag("title", meta.title ?: indexUrl.getPageUrl())
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

    private fun addBinaries() {}
}