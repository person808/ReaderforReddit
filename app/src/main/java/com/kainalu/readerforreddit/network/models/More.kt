package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A class that represents additional items that could be loaded, but are not
 * because there are too many items in the list.
 */
@JsonClass(generateAdapter = true)
data class More(
    val count: Int,
    val name: String,
    val id: String,
    @Json(name = "parent_id")
    val parentId: String,
    val depth: Int,
    val children: List<String>,
    @Transient
    override var hidden: Boolean = false
) : HideableSubmissionItem