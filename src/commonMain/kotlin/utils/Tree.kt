package utils

fun linkSetToTree(linkSet: Set<String>): Tree {
    val linkSetWithoutEmptyLink = linkSet - ""
    val root = Tree("")
    for (link in linkSetWithoutEmptyLink)
        root.makeTreeFromUrlParts(splitLink(link))

    return root
}


class Tree(
    val name: String,
    // using map to get Tree by name
    private val children: MutableMap<String, Tree> = mutableMapOf(),
    private var hasPage: Boolean = false
) {
    fun getPathsAlphabetically(currentUrl: StringBuilder = StringBuilder()): MutableList<String> {
        val res = mutableListOf<String>()
        val sortedChildren = children.values.sortedBy { it.name }
        currentUrl.append(name).append("/")

        for (child in sortedChildren) {
            val childUrls = child.getPathsAlphabetically(currentUrl)
            res.addAll(childUrls)
            if (child.hasPage)
                res.add(currentUrl.toString() + child.name)
        }

        currentUrl.deleteRange(currentUrl.length - name.length - 1, currentUrl.length)
        return res
    }

    fun addLink(link: Link) {
        val linkParts = splitLink(link)
        var currentTree = this
        while (linkParts.isNotEmpty()) {
            if (currentTree.children[linkParts.last()] != null) {
                currentTree = currentTree.children[linkParts.last()]!!
                linkParts.removeLast()
            } else {
                val nextCurrent = Tree(linkParts.last())
                currentTree.children[linkParts.last()] = nextCurrent
                linkParts.removeLast()
            }
        }
        currentTree.hasPage = true
    }

    fun makeTreeFromUrlParts(urlParts: MutableList<String>) {
        val firstPart = urlParts.removeLast()
        if (children.contains(firstPart) && urlParts.isNotEmpty())
            children[firstPart]!!.makeTreeFromUrlParts(urlParts)
        else {
            if (urlParts.isEmpty()) {
                children[firstPart] = Tree(firstPart, hasPage = true)
                return
            }
            val next = Tree(firstPart)
            children[firstPart] = next
            next.makeTreeFromUrlParts(urlParts)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tree)
            return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return printTree(0, StringBuilder()).toString()
    }

    private fun printTree(level: Int, sb: StringBuilder): StringBuilder {
        sb.append(" ".repeat(level)).append(name)
        if (hasPage)
            sb.append(" (x)\n")
        for (child in children.values)
            child.printTree(level + 2, sb)
        return sb
    }
}

private fun splitLink(link: Link): MutableList<String> {
    val linkParts = link.split("/").reversed().toMutableList()
    if (linkParts.last() == "")
        linkParts.removeLast()
    return linkParts
}
