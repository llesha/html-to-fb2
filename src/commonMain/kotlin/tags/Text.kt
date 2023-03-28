package tags

import Constants
import core.HtmlTranslator
import core.TraversableElement

class Text(element: TraversableElement) : Tag(element) {
    override fun getFb2(translator: HtmlTranslator): String {
        val (res, links, hasText) = element.getText()
        translator.addLinks(links)
        return if (hasText) res else ""
    }
}
