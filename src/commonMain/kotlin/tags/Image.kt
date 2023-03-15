package tags

import core.HtmlTranslator
import core.TraversableElement

class Image(element: TraversableElement) : Tag(element) {
    override fun getFb2(translator: HtmlTranslator): String {
        return "<p>image</p>"
    }
}
