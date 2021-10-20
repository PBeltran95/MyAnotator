package ar.com.myanotator.myanotator.repository

import androidx.lifecycle.LiveData
import ar.com.myanotator.myanotator.data.ToDoDao
import ar.com.myanotator.myanotator.data.models.ToDoData
import javax.inject.Inject

class ToDoRepository @Inject constructor (private val toDoDao: ToDoDao) {

    val getAllData:LiveData<List<ToDoData>> = toDoDao.getAllData()
    val sortByHighPriority : LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()
    val sortByLowPriority : LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()


    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData){
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

     fun searchData(searchQuery: String): LiveData<List<ToDoData>>{
        return toDoDao.searchDataBase(searchQuery)
    }

}