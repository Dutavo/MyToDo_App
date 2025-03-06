package br.com.dutavo.mytodoapp.ui.todo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.dutavo.mytodoapp.data.model.ToDo
import br.com.dutavo.mytodoapp.databinding.ActivityTodoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var adapter: ToDoAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val todoList = mutableListOf<ToDo>()
    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializa o view Binding
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configuração do RecycleView
        adapter = ToDoAdapter(
            todoList,
            onTodoChecked = { todo, isChecked -> updateTodoStatus(todo, isChecked) },
            onDeleteTodo = { todo -> deleteTodo(todo) }
        )


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        //Carregar tarefas do firestore
        loadTodos()

        //Adicionar nova tarefa ao Firestore
        binding.addButton.setOnClickListener {
            val todoText = binding.editTextTodo.text.toString()
            if (todoText.isNotEmpty()) {
                addTodo(todoText)
                binding.editTextTodo.text.clear()
            } else {
                Toast.makeText(this, "Adicione uma tarefa!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTodos() {
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("todos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
            todoList.clear()
            for (document in result) {
                val todo = document.toObject(ToDo::class.java)
                todoList.add(todo)
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar tarefas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addTodo(title: String) {
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }

        val todo = ToDo(
            id = UUID.randomUUID().toString(),
            title = title,
            completed = false,
            userId = userId!!
        )

        firestore.collection("todos").document(todo.id).set(todo)
            .addOnSuccessListener {
                todoList.add(todo)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTodo(todo: ToDo) {
        firestore.collection("todos").document(todo.id).delete()
            .addOnSuccessListener {
                todoList.remove(todo)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao excluir tarefa", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTodoStatus(todo: ToDo, isChecked: Boolean) {
        val todoRef = firestore.collection("todos").document(todo.id)
        todoRef.update("completed", isChecked)
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar status da tarefa", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}