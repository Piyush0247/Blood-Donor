package jain.piyush.blooddonor.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jain.piyush.blooddonor.roomdatabase.Person
import jain.piyush.blooddonor.roomdatabase.PersonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val personDao = PersonDatabase.getDatabase(application).personDao()

    // Using StateFlow for better state management
    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons = _persons.asStateFlow() // Exposing immutable StateFlow
    private val _completedPersons = MutableStateFlow<List<Person>>(emptyList())
    val completedPersons: Flow<List<Person>> = personDao.getCompletedPersons()

    init {
        loadPerson()
    }
    fun markPersonAsCompleted(person: Person) {
        viewModelScope.launch(Dispatchers.IO){
            _persons.value -= person
            _completedPersons.value += person
            personDao.updatePerson(person.copy(isCompleted = true))
        }
    }

    private fun loadPerson() {
        viewModelScope.launch(Dispatchers.IO) {  // ✅ Run on IO thread
            _persons.value = personDao.getAllPerson()
        }
    }

    fun addPerson(person: Person) {
        viewModelScope.launch(Dispatchers.IO) {
            personDao.insertPerson(person)
            loadPerson()
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch(Dispatchers.IO) {
            personDao.deletePerson(person)
            loadPerson()
        }
    }

    fun refreshState(){
        viewModelScope.launch(Dispatchers.IO) {
            loadPerson()
        }
    }
}
