package tags

import core.HtmlTranslator
import core.TraversableElement

class Text(element: TraversableElement) : Tag(element) {
    override fun getFb2(translator: HtmlTranslator): String {
        val res = element.getText()
        println(res)
        return res
    }
}
