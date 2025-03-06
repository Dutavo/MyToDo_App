package br.com.dutavo.mytodoapp.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.dutavo.mytodoapp.data.model.ToDo
import br.com.dutavo.mytodoapp.data.repository.ToDoRepository

/*class ToDoViewModel : ViewModel(){

    private val repository = ToDoRepository()
    private val _todos = MutableLiveData<List<ToDo>>()
    val todos: LiveData<List<ToDo>> get() = _todos

    init {
        fetchTodos()
    }

    fun fetchTodos(){
        repository.getTodos {
            _todos.postValue(it)
        }
    }

    fun addTodo(title: String) {
        val todo = ToDo(title = title, completed = false)
        repository.addTodo(todo) { success ->
            if (success) fetchTodos()
        }
    }

    fun updateTodo(todo: ToDo) {
        repository.updateTodo(todo) { success ->
            if (success) fetchTodos()
        }
    }

    fun deleteTodo(todoId: String) {
        repository.deleteTodo(todoId) { success ->
            if (success) fetchTodos()
        }
    }
}*/