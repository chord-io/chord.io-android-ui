package io.chord.ui

import android.app.Application
import io.chord.services.depencyinjection.sharedPreferencesAuthenticationStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChordIOApplication : Application()
{
	companion object Singleton
	{
		lateinit var instance: ChordIOApplication
	}
	
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@ChordIOApplication)
			modules(sharedPreferencesAuthenticationStorage)
		}
		
		instance = this
	}
}