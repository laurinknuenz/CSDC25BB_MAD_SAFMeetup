package at.csdc25bb.mad.safmeetup.di

import at.csdc25bb.mad.safmeetup.data.AppConstants
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSource
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSourceImpl
import at.csdc25bb.mad.safmeetup.data.repository.TeamRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
        }

        httpClient.readTimeout(60, TimeUnit.SECONDS)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesTeamDataSource(apiService: ApiService): TeamDataSource{
        return TeamDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesTeamRepository(teamDataSource: TeamDataSource): TeamRepository {
        return TeamRepository(teamDataSource)
    }
}