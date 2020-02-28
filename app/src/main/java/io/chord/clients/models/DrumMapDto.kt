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
    * @property id
    * @property name
    * @property map
*/

open class DrumMapDto(
    @Json(name = "id") @field:Json(name = "id") var id: String,
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "map") @field:Json(name = "map") var map: Map<String, String>
): Serializable, BaseModel()
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
