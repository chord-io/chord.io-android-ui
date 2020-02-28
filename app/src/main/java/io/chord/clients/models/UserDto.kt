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
    * @property username
    * @property email
    * @property password
*/

open class UserDto(
    @Json(name = "username") @field:Json(name = "username") var username: String,
    @Json(name = "email") @field:Json(name = "email") var email: String,
    @Json(name = "password") @field:Json(name = "password") var password: String
): Serializable, BaseModel()
{
    open fun copy(): UserDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as UserDto
    }
}
