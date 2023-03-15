package core

class XmlBuilder {
    private val content = StringBuilder()
    private val tags = mutableListOf<String>()

    fun addTag(tag: String): XmlBuilder {
        content.append("<").append(tag).append(">\n")
        tags.add(tag)
        return this
    }

    fun addTag(tag: String, content: String): XmlBuilder {
        addTag(tag).add(content).closeTag()
        return this
    }

    fun addTag(tag: String, block: () -> Unit): XmlBuilder {
        addTag(tag)
        block()
        closeTag()
        return this
    }

    fun closeTag(): XmlBuilder {
        content.append("</").append(tags.removeLast()).append(">\n")
        return this
    }

    fun add(text: String): XmlBuilder {
        content.append(text)
        return this
    }

    fun addTagWithAttributes(tag: String, vararg attributes: String): XmlBuilder {
        content.append("<").append(tag)
        for (attr in attributes)
            content.append(" ").append(attr)
        content.append(">")
        return this
    }

    fun getResult(): String = content.toString()

    fun clearWithoutTags(): XmlBuilder {
        content.clear()
        return this
    }

    override fun toString(): String {
        return content.toString()
    }
}
