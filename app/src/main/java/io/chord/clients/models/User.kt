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
    * @property id
    * @property username
    * @property email
*/

open class User(
    @Json(name = "id") @field:Json(name = "id") var id: String,
    @Json(name = "username") @field:Json(name = "username") var username: String,
    @Json(name = "email") @field:Json(name = "email") var email: String
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): User
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as User

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
