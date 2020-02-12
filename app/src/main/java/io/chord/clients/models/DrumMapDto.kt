package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property id
    * @property name
    * @property map
*/
@JsonClass(generateAdapter = true)
open class DrumMapDto(
    @Json(name = "id") @field:Json(name = "id") var id: kotlin.String,
    @Json(name = "name") @field:Json(name = "name") var name: kotlin.String,
    @Json(name = "map") @field:Json(name = "map") var map: kotlin.collections.Map<kotlin.String, kotlin.String>
)
{
    open fun copy(): DrumMapDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as DrumMapDto
    }
}

