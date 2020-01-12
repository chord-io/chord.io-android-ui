package io.chord.ui.models

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

class ViewModelFactory(
	vararg initArgs: Any
) : ViewModelProvider.NewInstanceFactory()
{
	private val initArgs: Any = initArgs
	
	override fun <T : ViewModel?> create(modelClass: Class<T>): T
	{
		if(AndroidViewModel::class.java.isAssignableFrom(modelClass))
		{
			try
			{
				return modelClass.getConstructor().newInstance(this.initArgs)
			}
			catch(e: InstantiationException)
			{
				e.printStackTrace()
			}
			catch(e: IllegalAccessException)
			{
				e.printStackTrace()
			}
			catch(e: InvocationTargetException)
			{
				e.printStackTrace()
			}
			catch(e: NoSuchMethodException)
			{
				e.printStackTrace()
			}
		}
		return super.create<T>(modelClass)
	}
}