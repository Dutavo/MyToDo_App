package br.com.dutavo.mytodoapp.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.dutavo.mytodoapp.model.ToDo
import br.com.dutavo.mytodoapp.databinding.ItemTodoBinding

class ToDoAdapter(
    private var todoList: MutableList<ToDo>,
    private val onTodoChecked: (ToDo, Boolean) -> Unit,
    private val onDeleteTodo: (ToDo) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: ToDo) {
            binding.todoTitle.text = todo.title
            binding.checkBox.isChecked = todo.completed

            //Atualiza status ao marcar/desmarcar checkbox
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                onTodoChecked(todo, isChecked)
            }

            //Remove tarefa ao clicar no botão de excluir
            binding.deleteButton.setOnClickListener {
                onDeleteTodo(todo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount() = todoList.size

    fun updateData(newTodoList: List<ToDo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

}