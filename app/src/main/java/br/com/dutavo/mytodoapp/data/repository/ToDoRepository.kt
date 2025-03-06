package br.com.dutavo.mytodoapp.data.repository

import br.com.dutavo.mytodoapp.data.model.ToDo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ToDoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val todoCollection = db.collection("users").document(userId).collection("todos")

    fun addTodo(todo: ToDo, onComplete: (Boolean) -> Unit) {
        val docRef = todoCollection.document()
        todo.id = docRef.id
        docRef.set(todo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateTodo(todo: ToDo, onComplete: (Boolean) -> Unit) {
        todoCollection.document(todo.id).set(todo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteTodo(todoId: String, onComplete: (Boolean) -> Unit) {
        todoCollection.document(todoId).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false)}
    }

    fun getTodos(onResult: (List<ToDo>) -> Unit) {
        todoCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val todos = it.toObjects(ToDo::class.java)
                }
            }
    }
}
