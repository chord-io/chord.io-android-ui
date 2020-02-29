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
    * @property position
    * @property length
    * @property pitch
*/

open class FingeringEntry(
    @Json(name = "position") @field:Json(name = "position") var position: Double,
    @Json(name = "length") @field:Json(name = "length") var length: Double,
    @Json(name = "pitch") @field:Json(name = "pitch") var pitch: Int
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): FingeringEntry
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as FingeringEntry

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
