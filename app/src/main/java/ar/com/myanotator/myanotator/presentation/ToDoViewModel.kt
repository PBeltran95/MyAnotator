package ar.com.myanotator.myanotator.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.myanotator.myanotator.data.ToDoDatabase
import ar.com.myanotator.myanotator.data.models.ToDoData
import ar.com.myanotator.myanotator.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor (private val repository: ToDoRepository): ViewModel() {


    val getAllData:LiveData<List<ToDoData>> = repository.getAllData

    val sortByHighPriority:LiveData<List<ToDoData>> = repository.sortByHighPriority

    val sortByLowPriority:LiveData<List<ToDoData>> = repository.sortByLowPriority

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(toDoData)
        }
    }
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun searchDataBase(searchQuery:String): LiveData<List<ToDoData>>{
        return repository.searchData(searchQuery)
    }
}