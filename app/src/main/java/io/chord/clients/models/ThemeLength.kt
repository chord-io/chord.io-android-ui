package io.chord.clients.models

import com.squareup.moshi.Json
import io.chord.clients.BaseModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property start
    * @property end
*/

open class ThemeLength(
    @Json(name = "start") @field:Json(name = "start") var start: Double,
    @Json(name = "end") @field:Json(name = "end") var end: Double
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): ThemeLength
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as ThemeLength

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
