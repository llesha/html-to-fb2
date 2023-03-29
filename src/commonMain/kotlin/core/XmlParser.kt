package core

expect class XmlParser() {
    fun parse(text: String): Pair<MetaInformation, TraversableElement>
    fun parseFromLink(link: String): Pair<MetaInformation, TraversableElement>?
}
