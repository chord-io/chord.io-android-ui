package io.chord.clients.models


import io.chord.clients.models.ThemeEntry
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property name
    * @property color
    * @property themes
*/

abstract class Track(
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "color") @field:Json(name = "color") var color: Int,
    @Json(name = "themes") @field:Json(name = "themes") var themes: List<ThemeEntry>
): Serializable
{
    open fun copy(): Track
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Track
    }
}
