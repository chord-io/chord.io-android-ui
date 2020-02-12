package io.chord.clients.models


import io.chord.clients.models.IntervalQuality
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property semitones
    * @property degree
    * @property quality
*/
@JsonClass(generateAdapter = true)
open class DegreeAndQualityIntervalDto(
    @Json(name = "semitones") @field:Json(name = "semitones") var semitones: kotlin.Int,
    @Json(name = "degree") @field:Json(name = "degree") var degree: kotlin.Int,
    @Json(name = "quality") @field:Json(name = "quality") var quality: IntervalQuality
)
{
    open fun copy(): DegreeAndQualityIntervalDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as DegreeAndQualityIntervalDto
    }
}

