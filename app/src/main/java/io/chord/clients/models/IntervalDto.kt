package io.chord.clients.models


import io.chord.clients.models.IntervalQuality
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property degree
    * @property semitones
    * @property quality
*/

open class IntervalDto(
    @Json(name = "degree") @field:Json(name = "degree") var degree: Int,
    @Json(name = "semitones") @field:Json(name = "semitones") var semitones: Int,
    @Json(name = "quality") @field:Json(name = "quality") var quality: IntervalQuality
): Serializable
{
    open fun copy(): IntervalDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as IntervalDto
    }
}
