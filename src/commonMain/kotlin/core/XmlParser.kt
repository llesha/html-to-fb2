package core

expect class XmlParser() {
    fun parse(text: String): Pair<MetaInformation, TraversableElement>
    fun parseFromLink(url: String): Pair<MetaInformation, TraversableElement>
}
