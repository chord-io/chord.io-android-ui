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
import io.chord.clients.BaseModel

/**
    * @property degree
    * @property semitones
    * @property quality
*/

open class SemitonesAndQualityIntervalDto(
    @Json(name = "degree") @field:Json(name = "degree") var degree: Int,
    @Json(name = "semitones") @field:Json(name = "semitones") var semitones: Int,
    @Json(name = "quality") @field:Json(name = "quality") var quality: IntervalQuality
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): SemitonesAndQualityIntervalDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as SemitonesAndQualityIntervalDto

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
