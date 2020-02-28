package io.chord.clients.models

import io.chord.clients.models.ThemeLength
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
    * @property index
    * @property length
*/

open class ThemeEntry(
    @Json(name = "index") @field:Json(name = "index") var index: Int,
    @Json(name = "length") @field:Json(name = "length") var length: ThemeLength
): Serializable, BaseModel()
{
    open fun copy(): ThemeEntry
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as ThemeEntry
    }
}
