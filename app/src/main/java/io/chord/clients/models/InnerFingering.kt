package io.chord.clients.models


import io.chord.clients.models.FingeringEntry
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property entries
*/
@JsonClass(generateAdapter = true)
open class InnerFingering(

    @Json(name = "entries") @field:Json(name = "entries") var entries: kotlin.Array<FingeringEntry>? = null)
{
    open fun copy(): InnerFingering
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as InnerFingering
    }
}

