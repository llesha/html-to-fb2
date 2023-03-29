import core.*
import utils.Date
import utils.Link
import utils.getPageUrl
import utils.linkSetToTree

class Fb2Document(private val indexLink: Link, path: String) {
    init {
        setCurrentSite(indexLink)
    }

    private val currentText = WriteStream(path)

    private val links = mutableSetOf<String>()
    private val binaries = mutableListOf<ByteArray>()
    private val traversed = mutableSetOf<String>()

    fun create() {
        currentText.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        currentText.add(
            XmlBuilder().addTagWithAttributes(
                "FictionBook",
                "xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\"",
                "xmlns:l=\"http://www.w3.org/1999/xlink\""
            ).getResult()
        )

        createDescription(
            XmlParser().parseFromLink(indexLink)?.first ?: throw NullPointerException("given url not found")
        )

        if (Constants.isTree) {
            val orderedLinks = linkSetToTree(collectLinks(indexLink)).getPathsAlphabetically()
            links.addAll(orderedLinks)
            println(links.size)
            for (link in orderedLinks)
                traverseHtml(Constants.currentSite + link, 0)
        } else {
            var current = indexLink
            links.add(current)
            while (links.isNotEmpty()) {
                current = links.first()
                println(current)
                traversed.add(current)
                traverseHtml(current, 0)
                links.remove(current)
            }
        }

        addBinaries()

        currentText.add("</FictionBook>")
        currentText.close()
    }

    private fun collectLinks(link: Link): Set<String> {
        val res = mutableSetOf(link)
        val addedToStack = mutableSetOf(link)
        val stackToTraverse = mutableListOf(link to 0)

        while (stackToTraverse.isNotEmpty()) {
            val (currentLink, level) = stackToTraverse.removeLast()
            res.add(currentLink)
            println(currentLink)

            if (level + 1 <= Constants.linkLevel || Constants.linkLevel == -1) {
                val currentChildLinks =
                    (HtmlTranslator(currentLink, currentText).getLinks() - addedToStack)
                addedToStack.addAll(currentChildLinks)
                for (child in currentChildLinks)
                    stackToTraverse.add((Constants.currentSite + child) to (level + 1))
            }
        }
        return res
    }


    private fun setCurrentSite(path: String) {
        val trimmed = path.removePrefix("https://").removePrefix("http://")
        val indexOfSlash = trimmed.indexOf("/")
        Constants.currentSite = "https://" + (if (indexOfSlash == -1) trimmed else trimmed.substring(0, indexOfSlash))
    }

    private fun traverseHtml(link: Link, level: Int) {
        val bodyBuilder = XmlBuilder().addTag("body").addTag("section")
        currentText.add(bodyBuilder.getResult())

        val (htmlLinks, binaries) = HtmlTranslator(link, currentText).translateToFb2()

        currentText.add(bodyBuilder.clearWithoutTags().closeTag().closeTag().getResult())
        if (level < Constants.linkLevel || Constants.linkLevel == -1)
            links.addAll(htmlLinks.map { Constants.currentSite + it } - traversed)
        this.binaries.addAll(binaries)
    }

    private fun createDescription(meta: MetaInformation) {
        val xmlBuilder = XmlBuilder()
        xmlBuilder.addTag("description") {
            xmlBuilder.addTag("title-info") {
                xmlBuilder.addTag("title", meta.title ?: indexLink.getPageUrl())
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