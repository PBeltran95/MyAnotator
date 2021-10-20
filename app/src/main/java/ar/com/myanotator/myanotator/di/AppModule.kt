package ar.com.myanotator.myanotator.di

import android.content.Context
import androidx.room.Room
import ar.com.myanotator.myanotator.data.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            "todo_database").build()


    @Singleton
    @Provides
    fun providesDao(db:ToDoDatabase) = db.toDoDao()
}