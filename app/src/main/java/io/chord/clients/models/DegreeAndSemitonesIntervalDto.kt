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
    * @property quality
    * @property degree
    * @property semitones
*/

open class DegreeAndSemitonesIntervalDto(
    @Json(name = "quality") @field:Json(name = "quality") var quality: IntervalQuality,
    @Json(name = "degree") @field:Json(name = "degree") var degree: Int,
    @Json(name = "semitones") @field:Json(name = "semitones") var semitones: Int
): Serializable
{
    open fun copy(): DegreeAndSemitonesIntervalDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as DegreeAndSemitonesIntervalDto
    }
}
