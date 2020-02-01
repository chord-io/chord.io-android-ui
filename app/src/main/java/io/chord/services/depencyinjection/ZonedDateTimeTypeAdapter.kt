package io.chord.services.depencyinjection

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import org.threeten.bp.ZonedDateTime

class ZonedDateTimeTypeAdapter : TypeAdapter<ZonedDateTime>()
{
	override fun write(
		writer: JsonWriter?,
		value: ZonedDateTime?
	)
	{
		writer!!.value(value.toString())
	}
	
	override fun read(reader: com.google.gson.stream.JsonReader?): ZonedDateTime
	{
		return ZonedDateTime.parse(reader!!.nextString())
	}
	
}