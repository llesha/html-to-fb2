package tags

import core.HtmlTranslator
import core.TraversableElement

class Link(element: TraversableElement) : Tag(element) {
    override fun getFb2(translator: HtmlTranslator): String {
        return "<p>link</p>"
    }
}
