package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import io.chord.clients.BaseModel

/**
* 
* Values: Chord,Drum
*/
enum class FingeringType(val value: kotlin.String){

    @Json(name = "Chord") Chord("Chord"),

    @Json(name = "Drum") Drum("Drum");

}

