package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
* 
* Values: Diminished,Minor,Perfect,Major,Augmented
*/
enum class IntervalQuality(val value: kotlin.String){

    @Json(name = "Diminished") Diminished("Diminished"),

    @Json(name = "Minor") Minor("Minor"),

    @Json(name = "Perfect") Perfect("Perfect"),

    @Json(name = "Major") Major("Major"),

    @Json(name = "Augmented") Augmented("Augmented");

}

