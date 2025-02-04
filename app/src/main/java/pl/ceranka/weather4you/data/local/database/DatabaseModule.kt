package pl.ceranka.weather4you.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.ceranka.weather4you.data.local.dao.CityDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Weather4YouDatabase =
        Room.databaseBuilder(
            context,
            Weather4YouDatabase::class.java,
            "weather4you.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideCityDao(database: Weather4YouDatabase): CityDao = database.cityDao()
}