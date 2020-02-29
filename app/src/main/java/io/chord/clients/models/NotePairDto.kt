package io.chord.clients.models

import io.chord.clients.models.NoteDto
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
    * @property a
    * @property b
*/

open class NotePairDto(
    @Json(name = "a") @field:Json(name = "a") var a: NoteDto,
    @Json(name = "b") @field:Json(name = "b") var b: NoteDto
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): NotePairDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as NotePairDto

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
