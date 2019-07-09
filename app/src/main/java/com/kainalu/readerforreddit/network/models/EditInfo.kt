package com.kainalu.readerforreddit.network.models

import org.threeten.bp.LocalDateTime

sealed class EditInfo
data class Edited(val time: LocalDateTime): EditInfo()
data class NotEdited(val edited: Boolean = false): EditInfo()


