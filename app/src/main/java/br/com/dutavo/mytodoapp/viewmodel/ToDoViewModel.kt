package br.com.dutavo.mytodoapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.dutavo.mytodoapp.model.ToDo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ToDoViewModel : ViewModel(){
    private val firestore = FirebaseFirestore.getInstance()
    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid
    private val _todoList = MutableLiveData<List<ToDo>>()
    val todoList: MutableLiveData<List<ToDo>> get() = _todoList

    init {
        loadTodos()
    }

    fun loadTodos() {
        userId?.let { uid ->
            firestore.collection("todos")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { result ->
                    val todos = result.toObjects(ToDo::class.java)
                    _todoList.value = todos
                }
                .addOnFailureListener {
                    Log.e("ToDoViewModel", "Erro ao carregar tarefas", it)
                }
        }
    }

    fun addTodo(title: String) {
        userId?.let { uid ->
            val todo = ToDo(
                id = UUID.randomUUID().toString(),
                title = title,
                completed = false,
                userId = uid
            )
            firestore.collection("todos").document(todo.id).set(todo)
                .addOnSuccessListener {
                    //Atualiza a lista de tarefas
                    _todoList.value = _todoList.value?.plus(todo) ?: listOf(todo)
                }
                //Tratar erros
                .addOnFailureListener {
                    Log.e("ToDoViewModel", "Erro ao adicionar tarefa", it)

                }
        }
    }

    fun deleteTodo(todo: ToDo){
        firestore.collection("todos").document(todo.id).delete()
            .addOnSuccessListener {
                _todoList.value = _todoList.value?.filter { it.id != todo.id }
            }
            //Tratar erros
            .addOnFailureListener {
                Log.e("ToDoViewModel", "Erro ao excluir tarefa", it)

            }
    }

    fun updateTodoStatus(todo: ToDo, isChecked: Boolean) {
        firestore.collection("todos").document(todo.id)
            .update("completed", isChecked)
            .addOnSuccessListener {
                val updatedTodoList = _todoList.value?.map {
                    if (it.id == todo.id) it.copy(completed = isChecked) else it
                }
                _todoList.value = updatedTodoList
            }
            //Tratar erros
            .addOnFailureListener {
                Log.e("ToDoViewModel", "Erro ao atualizar status da tarefa", it)
            }
    }

    fun logoutUser(): Boolean {
        FirebaseAuth.getInstance().signOut()
        return true
    }
}