package io.chord.ui.behaviors

import kotlin.reflect.KClass

class FragmentTransactionManager
{
	private val transactions: MutableMap<KClass<*>, FragmentTransation> = mutableMapOf()
	
	fun add(cls: KClass<*>, transaction: FragmentTransation)
	{
		if(!this.transactions.containsKey(cls))
		{
			transactions[cls] = transaction
		}
	}
	
	fun remove(cls: KClass<*>)
	{
		if(this.transactions.containsKey(cls))
		{
			this.transactions.remove(cls)
		}
	}
	
	fun from(obj: Any): FragmentTransation
	{
		return this.transactions.getValue(obj::class)
	}
	
	fun all(): List<FragmentTransation>
	{
		return this.transactions.values.toList()
	}
}