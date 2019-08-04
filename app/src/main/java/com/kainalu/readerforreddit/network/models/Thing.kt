package com.kainalu.readerforreddit.network.models

/**
 * Reddit's base class for most objects
 */
interface Thing {
    val id: String
    val name: String
}