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
    * @property key
    * @property picth
    * @property alteration
*/

open class NoteDto(
    @Json(name = "key") @field:Json(name = "key") var key: String,
    @Json(name = "picth") @field:Json(name = "picth") var picth: Int,
    @Json(name = "alteration") @field:Json(name = "alteration") var alteration: Int
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): NoteDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as NoteDto

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
