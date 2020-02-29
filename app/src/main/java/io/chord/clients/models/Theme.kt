package io.chord.clients.models

import io.chord.clients.models.Sequence
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
    * @property sequences
*/

open class Theme(
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "sequences") @field:Json(name = "sequences") var sequences: List<Sequence>
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): Theme
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as Theme

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
