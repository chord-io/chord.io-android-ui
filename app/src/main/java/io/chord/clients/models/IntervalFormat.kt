package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
* 
* Values: Short,Medium,Formula
*/
enum class IntervalFormat(val value: kotlin.String){

    @Json(name = "Short") Short("Short"),

    @Json(name = "Medium") Medium("Medium"),

    @Json(name = "Formula") Formula("Formula");

}

