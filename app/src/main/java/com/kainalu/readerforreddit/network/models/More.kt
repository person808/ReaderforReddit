package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A class that represents additional items that could be loaded, but are not
 * because there are too many items in the list.
 */
@JsonClass(generateAdapter = true)
data class More(
    override val id: String,
    override val name: String,
    val children: List<String>,
    val count: Int,
    val depth: Int,
    @Json(name = "parent_id")
    val parentId: String
) : SubmissionItem
