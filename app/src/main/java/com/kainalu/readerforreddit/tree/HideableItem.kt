package com.kainalu.readerforreddit.tree

interface HideableItem {
    var visibility: VisibilityState
}

/**
 * Represents the visibility of items in the comment tree.
 */
enum class VisibilityState {
    /** A visible item */
    VISIBLE,
    /** A collapsed item */
    COLLAPSED,
    /** A hidden item */
    HIDDEN,
    /**
     * A collapsed item that is hidden. When shown again, this item
     * should be marked as [COLLAPSED] instead of visible
     */
    COLLAPSED_HIDDEN
}