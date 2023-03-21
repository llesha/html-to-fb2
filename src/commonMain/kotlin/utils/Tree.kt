package utils

class Tree(
    val name: String,
    // using map to get Tree by name
    val children: MutableMap<String, Tree> = mutableMapOf(),
    var hasPage: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Tree)
            return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun addLink(link: String) {
        val urlParts = link.split("/")
        var currentTree = this
        for (part in urlParts) {

        }
    }

    fun makeTreeFromParts(parts: MutableList<String>) {
        if (parts.isEmpty()) {
            hasPage = true
            return
        }
        val current = parts.removeLast()
        if (children.contains(current))
            children[current]!!.makeTreeFromParts(parts)
        else {
            val next = Tree(current)
            children[current] = next
            next.makeTreeFromParts(parts)
        }
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

fun linkSetToTree(linkSet: Set<String>): Tree {
    val root = Tree("")
    for (link in linkSet) {
        root.makeTreeFromParts(link.split("/").reversed().toMutableList())
    }
    return root
}
