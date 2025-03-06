package br.com.dutavo.mytodoapp.model

data class ToDo(
    var id: String = "",
    var title: String = "",
    var completed: Boolean = false,
    var userId: String = ""
)
