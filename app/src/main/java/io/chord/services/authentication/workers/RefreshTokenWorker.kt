package io.chord.services.authentication.workers

import android.content.Context
import androidx.work.*
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE
import io.chord.clients.ClientApi
import io.chord.clients.apis.AuthenticationApi
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

class RefreshTokenWorker(
	context: Context,
	parameters: WorkerParameters
) : Worker(context, parameters), KoinComponent
{
	companion object {
		private const val workName: String = "refresh-token-work"
		
		fun start(repeatInterval: Long) {
			val workConstraints = Constraints.Builder()
				.setRequiredNetworkType(NetworkType.CONNECTED)
				.build()
			
			val request = PeriodicWorkRequest.Builder(
				RefreshTokenWorker::class.java,
				repeatInterval,
				TimeUnit.SECONDS
			)
				.setConstraints(workConstraints)
				.build()
			
			WorkManager.getInstance().enqueueUniquePeriodicWork(
				workName,
				REPLACE,
				request
			)
		}
		
		fun cancel()
		{
			WorkManager.getInstance().cancelUniqueWork(workName)
		}
	}
	
	private val storage: SharedPreferencesAuthenticationStorage by inject()
	private val api: AuthenticationApi = ClientApi.getAuthenticationApi()
	
	override fun doWork(): Result
	{
		if(!this.storage.contains())
		{
			return Result.failure()
		}
		
		val authentication = this.storage.retrieve()
		
		if(authentication == null || authentication.refreshToken !!.isEmpty())
		{
			return Result.failure()
		}
		
		return try
		{
			runBlocking { api.refresh(authentication.refreshToken) }
			Result.success()
		}
		catch(exception: Exception)
		{
			Result.retry()
		}
	}
}