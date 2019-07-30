package com.kainalu.readerforreddit.network.models

interface SubmissionItem

interface HideableSubmissionItem : SubmissionItem {
    var hidden: Boolean
}
