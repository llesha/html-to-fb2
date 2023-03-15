package tags

import core.HtmlTranslator
import core.TraversableElement

abstract class Tag(val element: TraversableElement) {
    abstract fun getFb2(translator: HtmlTranslator): String
}