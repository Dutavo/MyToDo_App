package br.com.dutavo.mytodoapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.dutavo.mytodoapp.databinding.ActivityTodoBinding
import br.com.dutavo.mytodoapp.ui.todo.ToDoAdapter
import br.com.dutavo.mytodoapp.viewmodel.ToDoViewModel

class ToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var adapter: ToDoAdapter

    //private val firestore = FirebaseFirestore.getInstance()

    //private val todoList = mutableListOf<ToDo>()
    //private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializa o view Binding
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configuração do RecycleView
        adapter = ToDoAdapter(
            mutableListOf(),
            onTodoChecked = { todo, isChecked -> viewModel.updateTodoStatus(todo, isChecked) },
            onDeleteTodo = { todo -> viewModel.deleteTodo(todo) }
        )


        //Configuração do RecycleView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        // Observando mudanças na lista de tarefas
        viewModel.todoList.observe(this) { todos ->
            adapter.updateData(todos) // Atualiza a RecyclerView de forma eficiente
        }

        //Adicionar nova tarefa ao Firestore
        binding.addButton.setOnClickListener {
            val todoText = binding.editTextTodo.text.toString()
            if (todoText.isNotEmpty()) {
                viewModel.addTodo(todoText)
                binding.editTextTodo.text.clear()
            } else {
                Toast.makeText(this, "Adicione uma tarefa!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.logoutButton.setOnClickListener {
            if (viewModel.logoutUser()) {
                Toast.makeText(this, "Logout bem-sucedido", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}

    /*//Função para carregar tarefas do Firestore
    private fun loadTodos() {
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        //Carrega tarefas do usuário atual
        firestore.collection("todos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
            //Limpa a lista de tarefas e adiciona as novas tarefas
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

    //Função para adicionar uma nova tarefa
    private fun addTodo(title: String) {
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }

        //Cria uma nova tarefa com um ID único
        val todo = ToDo(
            id = UUID.randomUUID().toString(),
            title = title,
            completed = false,
            userId = userId!!
        )

        //Adiciona a tarefa ao Firestore
        firestore.collection("todos").document(todo.id).set(todo)
            .addOnSuccessListener {
                todoList.add(todo)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show()
            }
    }

    //Função para excluir uma tarefa
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

    //Função para atualizar o status da tarefa
    private fun updateTodoStatus(todo: ToDo, isChecked: Boolean) {
        val todoRef = firestore.collection("todos").document(todo.id)
        todoRef.update("completed", isChecked)
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar status da tarefa", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    //Função para fazer logout do usuário
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut() // Faz o logout do usuário
        Toast.makeText(this, "Logout bem-sucedido", Toast.LENGTH_SHORT).show()

        // Volta para a tela de Login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Fecha a atividade atual para evitar que o usuário volte pressionando "Voltar"
    }
}*/