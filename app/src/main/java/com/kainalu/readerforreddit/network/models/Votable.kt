package com.kainalu.readerforreddit.network.models

/**
 * Interface that all objects that can be voted on implement
 */
interface Votable {
    val ups: Int
    val downs: Int
    val liked: Boolean?
}