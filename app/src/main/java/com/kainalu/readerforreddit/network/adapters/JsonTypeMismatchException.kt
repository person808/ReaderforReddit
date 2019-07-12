package com.kainalu.readerforreddit.network.adapters

/**
 * Thrown when the `kind` property of the Enveloped json object we are trying to deserialize
 * does not match the expected `kind` of object we want. For example if we are trying to
 * deserialize a [Comment] but the `kind` property indicates that the JSON data contains
 * a [Link], then this exception should be thrown.
 */
class JsonTypeMismatchException : RuntimeException {

    constructor()

    constructor(message: String?) : super(message)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
