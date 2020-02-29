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
    * @property password
*/

open class SignInDto(
    @Json(name = "username") @field:Json(name = "username") var username: String,
    @Json(name = "password") @field:Json(name = "password") var password: String
): Serializable, BaseModel()
{
    open fun copy(regenerate: Boolean = false): SignInDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as SignInDto

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
