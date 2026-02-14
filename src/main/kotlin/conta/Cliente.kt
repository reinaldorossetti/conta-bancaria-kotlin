package conta

data class Cliente(
    val id: Int,
    val nome: String,
    val cpf: String,
    val email: String?
) {
    init {
        require(id > 0) { "ID deve ser positivo" }
        require(nome.isNotBlank()) { "Nome não pode ser vazio" }
        require(cpf.matches(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))) { "CPF inválido" }
    }
}
