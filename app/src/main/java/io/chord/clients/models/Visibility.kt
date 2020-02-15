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
* Values: Public,Private
*/
enum class Visibility(val value: kotlin.String){

    @Json(name = "Public") Public("Public"),

    @Json(name = "Private") Private("Private");

}

