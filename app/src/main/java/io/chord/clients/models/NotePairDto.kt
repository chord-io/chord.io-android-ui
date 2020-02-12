package io.chord.clients.models


import io.chord.clients.models.NoteDto
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property a
    * @property b
*/
@JsonClass(generateAdapter = true)
open class NotePairDto(
    @Json(name = "a") @field:Json(name = "a") var a: NoteDto,
    @Json(name = "b") @field:Json(name = "b") var b: NoteDto
)
{
    open fun copy(): NotePairDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as NotePairDto
    }
}

