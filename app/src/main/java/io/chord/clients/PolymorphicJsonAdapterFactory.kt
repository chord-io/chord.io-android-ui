package io.chord.clients

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class PolymorphicJsonAdapterFactory<T> private constructor(
	private val type: Class<T>
) : JsonAdapter.Factory
{
	private val subtypes: MutableMap<Type, String> = mutableMapOf()
	
	companion object
	{
		fun <T> of(type: Class<T>) : PolymorphicJsonAdapterFactory<T>
		{
			return PolymorphicJsonAdapterFactory(type)
		}
	}
	
	fun withSubtype(type: Type, propertyKey: String)
	{
		this.subtypes[type] = propertyKey
	}
	
	override fun create(
		type: Type,
		annotations: MutableSet<out Annotation>,
		moshi: Moshi
	): JsonAdapter<*>?
	{
		if(Types.getRawType(type) != this.type || annotations.isNotEmpty())
		{
			return null
		}
		
		val adapters: MutableList<JsonAdapter<Any>> = mutableListOf()
		
		this.subtypes.forEach {
			val adapter = moshi.adapter<Any>(it.key)
			adapters.add(adapter)
		}
		
		return PolymorphicJsonAdapter(this.subtypes, adapters.toList())
	}
	
	class PolymorphicJsonAdapter(
		private val subtypes: MutableMap<Type, String>,
		private val adapters: List<JsonAdapter<Any>>
	) : JsonAdapter<Any>()
	{
		override fun fromJson(reader: JsonReader): Any?
		{
			this.subtypes.forEach {
				val index = propertyIndex(reader, it.value)
				
				if(index != -1)
				{
					return this.adapters[index].fromJson(reader)
				}
			}
			
			return null
		}
		
		override fun toJson(writer: JsonWriter, value: Any?)
		{
			val type: Type = value!!.javaClass
			val index: Int = this.subtypes.keys.indexOf(type)
			require(index != -1) {
				("Expected one of "
				 + subtypes
				 + " but found "
				 + value
				 + ", a "
				 + value.javaClass
				 + ". Register this subtype.")
			}
			val adapter: JsonAdapter<Any> = this.adapters[index]
			adapter.toJson(writer, value)
		}
		
		private fun propertyIndex(reader: JsonReader, propertyKey: String): Int
		{
			val option = JsonReader.Options.of(propertyKey)
			val peeked = reader.peekJson()
			peeked.setFailOnUnknown(false)
			peeked.beginObject()
			
			while(peeked.hasNext())
			{
				val index = peeked.selectName(option)
				
				if(index == -1)
				{
					peeked.skipName()
					peeked.skipValue()
					continue
				}
				
				peeked.close()
				
				return index
			}
			
			peeked.close()
			
			return -1
		}
	}
}