package io.chord.clients.models

import com.squareup.moshi.Json
import io.chord.clients.BaseModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property index
    * @property length
*/

open class ThemeEntry(
    @Json(name = "index") @field:Json(name = "index") var index: Int,
    @Json(name = "length") @field:Json(name = "length") var length: ThemeLength
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): ThemeEntry
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as ThemeEntry

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
