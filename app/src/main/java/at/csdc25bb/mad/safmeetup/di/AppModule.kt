package at.csdc25bb.mad.safmeetup.di

import android.content.Context
import at.csdc25bb.mad.safmeetup.SFMApplication
import at.csdc25bb.mad.safmeetup.data.AppConstants
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.HeaderInterceptor
import at.csdc25bb.mad.safmeetup.data.datasource.ActivityDataSource
import at.csdc25bb.mad.safmeetup.data.datasource.ActivityDataSourceImpl
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSource
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSourceImpl
import at.csdc25bb.mad.safmeetup.data.datasource.UserDataSource
import at.csdc25bb.mad.safmeetup.data.datasource.UserDataSourceImpl
import at.csdc25bb.mad.safmeetup.data.repository.ActivityRepository
import at.csdc25bb.mad.safmeetup.data.repository.TeamRepository
import at.csdc25bb.mad.safmeetup.data.repository.UserRepository
import at.csdc25bb.mad.safmeetup.ui.viewmodel.ActivityViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.AuthViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel
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
        val sharedPref = SFMApplication.instance.getSharedPreferences("SFMApplication", Context.MODE_PRIVATE)
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient().newBuilder()
            .apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor(sharedPref))
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

    @Provides
    @Singleton
    fun providesTeamViewModel(teamRepository: TeamRepository): TeamViewModel{
        return TeamViewModel(teamRepository)
    }

    @Provides
    @Singleton
    fun providesUserDataSource(apiService: ApiService): UserDataSource {
        return UserDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesUserRepository(apiService: ApiService, userDataSource: UserDataSource): UserRepository {
        return UserRepository(apiService, userDataSource)
    }

    @Provides
    @Singleton
    fun providesLoginViewModel(userRepository: UserRepository): AuthViewModel {
        return AuthViewModel(userRepository)
    }

    @Provides
    @Singleton
    fun providesActivityDataSource(apiService: ApiService): ActivityDataSource{
        return ActivityDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesActivityRepository(activityDataSource: ActivityDataSource): ActivityRepository {
        return ActivityRepository(activityDataSource)
    }

    @Provides
    @Singleton
    fun providesActivityViewModel(activityRepository: ActivityRepository): ActivityViewModel {
        return ActivityViewModel(activityRepository)
    }

}