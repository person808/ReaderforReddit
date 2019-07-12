package com.kainalu.readerforreddit

import com.kainalu.readerforreddit.network.adapters.EditInfoJsonAdapter
import com.kainalu.readerforreddit.network.adapters.LocalDateTimeJsonAdapter
import com.kainalu.readerforreddit.network.models.EditInfo
import com.kainalu.readerforreddit.network.models.Edited
import com.kainalu.readerforreddit.network.models.NotEdited
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AdapterTest {

    private val epochSeconds = 1538636400000
    private val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.of("UTC"))
    val moshi = Moshi.Builder()
        .add(EditInfoJsonAdapter())
        .add(LocalDateTimeJsonAdapter())
        .build()

    @Test
    fun editInfoAdapterTest() {
        val adapter = moshi.adapter(EditInfo::class.java)

        val jsonEdited = "$epochSeconds"
        val expectedEdited = Edited(localDateTime)
        val resultEdited = adapter.fromJson(jsonEdited)
        assertEquals(expectedEdited, resultEdited)
        assertEquals(jsonEdited, adapter.toJson(resultEdited))

        val jsonNotEdited = "false"
        val resultNotEdited = adapter.fromJson(jsonNotEdited)
        assertEquals(NotEdited(), resultNotEdited)
        assertEquals(jsonNotEdited, adapter.toJson(resultNotEdited))
    }

    @Test
    fun localDateTimeAdapterTest() {
        val adapter = moshi.adapter(LocalDateTime::class.java)
        val json = "$epochSeconds"
        val result = adapter.fromJson(json)
        assertEquals(localDateTime, result)
        assertEquals(json, adapter.toJson(result))
    }
}

