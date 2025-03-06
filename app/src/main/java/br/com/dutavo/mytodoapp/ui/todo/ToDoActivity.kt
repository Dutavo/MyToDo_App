package br.com.dutavo.mytodoapp.ui.todo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.dutavo.mytodoapp.R
import br.com.dutavo.mytodoapp.data.model.ToDo
import br.com.dutavo.mytodoapp.databinding.ActivityTodoBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var adapter: ToDoAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val todoList = mutableListOf<ToDo>()

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
        firestore.collection("todos").get().addOnSuccessListener { result ->
            todoList.clear()
            for (document in result) {
                val todo = document.toObject(ToDo::class.java)
                todoList.add(todo)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun addTodo(title: String) {
        val todo = ToDo(
            id = UUID.randomUUID().toString(),
            title = title,
            completed = false,
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













    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        adapter = ToDoAdapter(emptyList(),
            onTodoChecked = { viewModel.updateTodo(it) },
            onDelete = { viewModel.deleteTodo(it) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.todos.observe(this) { adapter.updateList(it) }

        binding.addButton.setOnClickListener {
            val todoTitle = binding.editTextTodo.text.toString()
            if (todoTitle.isNotEmpty()) {
                viewModel.addTodo(todoTitle)
                binding.editTextTodo.text.clear()
            }
        }

        addButton.setOnClickListener {
            val todoTitle = editTextTodo.text.toString()
            if (todoTitle.isNotEmpty()) {
                viewModel.addTodo(todoTitle)
                editTextTodo.text.clear()
            }
        }
    }*/
