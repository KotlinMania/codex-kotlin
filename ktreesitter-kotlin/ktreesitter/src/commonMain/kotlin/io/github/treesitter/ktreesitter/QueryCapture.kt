package io.github.treesitter.ktreesitter

/**
 * A [Node] that was captured with a certain capture [name].
 *
 * @property node The captured node.
 * @property name The name of the capture.
 */
@ConsistentCopyVisibility
data class QueryCapture internal constructor(
    val node: Node,
    val name: String
) {
    override fun toString() = "QueryCapture(name=$name, node=$node)"
}
