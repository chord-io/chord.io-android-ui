package io.chord.clients.models

import io.chord.clients.models.FingeringEntry
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
    * @property entries
*/

open class InnerFingering(

    @Json(name = "entries") @field:Json(name = "entries") var entries: List<FingeringEntry>? = null): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): InnerFingering
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as InnerFingering

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
