package io.chord.clients

import io.chord.R
import io.chord.clients.apis.*
import io.chord.clients.tools.GeneratedCodeConverters
import io.chord.ui.ChordIOApplication
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class ClientApi {
    companion object {
        private fun getRetroFit(clientBuilder: OkHttpClient.Builder? = null): Retrofit
        {
            val builder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GeneratedCodeConverters.converterFactory())
                .baseUrl(ChordIOApplication.instance.resources.getString(R.string.api_base_url))
            
            if(clientBuilder != null)
            {
                builder.client(clientBuilder.build())
            }
            else
            {
                builder.client(this.getHttpClientBuilder().build())
            }
            
            return builder.build()
        }
        
        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor
        {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }
        
        private fun getHttpClientBuilder(): OkHttpClient.Builder
        {
            return OkHttpClient.Builder()
                .addInterceptor(this.getHttpLoggingInterceptor())
        }
        
        private fun getAuthorizedClient(): OkHttpClient.Builder
        {
            return this.getHttpClientBuilder()
                .addInterceptor(AuthorizeClientInterceptor())
        }

        fun getArithmeticIntervalApi(): ArithmeticIntervalApi = this.getRetroFit().create(ArithmeticIntervalApi::class.java)

        fun getArithmeticNoteApi(): ArithmeticNoteApi = this.getRetroFit().create(ArithmeticNoteApi::class.java)

        fun getProjectsApi(): ProjectsApi = this.getRetroFit(this.getAuthorizedClient()).create(ProjectsApi::class.java)
    
        fun getUserApi(): UsersApi = this.getRetroFit(this.getAuthorizedClient()).create(UsersApi::class.java)
    
        fun getAuthenticationApi(): AuthenticationApi = this.getRetroFit().create(AuthenticationApi::class.java)
    }

}