package io.chord.clients.models

import io.chord.clients.models.Theme
import io.chord.clients.models.ThemeEntry
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
    * @property name
    * @property color
    * @property themes
    * @property entries
*/

abstract class Track(
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "color") @field:Json(name = "color") var color: Int,
    @Json(name = "themes") @field:Json(name = "themes") var themes: List<Theme>,
    @Json(name = "entries") @field:Json(name = "entries") var entries: List<ThemeEntry>
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): Track
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as Track

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
