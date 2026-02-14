package conta

/**
 * Interface que define o contrato de autenticação para clientes do sistema bancário.
 *
 * Implementações devem garantir validação rigorosa de credenciais, incluindo:
 * - Verificação de identificador único (CPF para PF, CNPJ para PJ)
 * - Validação de senha forte conforme políticas de segurança
 * - Controle de tentativas de autenticação
 *
 * @see ClientePF Implementação para pessoa física
 * @see ClientePJ Implementação para pessoa jurídica
 */
interface Autenticavel {
    /**
     * Número máximo de tentativas de autenticação permitidas antes de bloqueio.
     * Valor padrão: 3 tentativas.
     */
    val tentativasMaximas: Int get() = 3

    /**
     * Autentica um usuário no sistema bancário com base em identificador e senha.
     *
     * @param usuario Identificador único do cliente (CPF ou CNPJ, com ou sem formatação)
     * @param senha Senha do usuário que deve atender aos critérios de segurança
     * @return `true` se as credenciais são válidas e a autenticação foi bem-sucedida, `false` caso contrário
     *
     * @see validarSenhaForte para requisitos de senha
     */
    fun autenticar(usuario: String, senha: String): Boolean

    /**
     * Valida se uma senha atende aos requisitos de segurança baseados em OWASP ASVS.
     *
     * Critérios de validação:
     * - Comprimento mínimo de 12 caracteres
     * - Pelo menos uma letra maiúscula (A-Z)
     * - Pelo menos uma letra minúscula (a-z)
     * - Pelo menos um dígito numérico (0-9)
     * - Pelo menos um caractere especial (!@#$%^&*()_-+=[]{}|;:'",.<>/?`~)
     * - Sem espaços em branco
     * - Não pode ser uma senha comum/trivial conhecida
     *
     * @param senha String contendo a senha a ser validada
     * @return `true` se a senha atende a todos os requisitos de segurança, `false` caso contrário
     *
     * @see <a href="https://owasp.org/www-project-application-security-verification-standard/">OWASP ASVS</a>
     */
    fun validarSenhaForte(senha: String): Boolean {
        // Regras inspiradas em OWASP ASVS: tamanho mínimo, tipos de caracteres e ausência de espaços
        val minLengthOk = senha.length >= 12
        val hasUpper = senha.any { it.isUpperCase() }
        val hasLower = senha.any { it.isLowerCase() }
        val hasDigit = senha.any { it.isDigit() }
        val hasSpecial = senha.any { it in "!@#\$%^&*()_-+=[]{}|;:'\",.<>/?`~" }
        val noSpaces = senha.none { it.isWhitespace() }
        val notCommon = senha.lowercase() !in setOf("password", "123456", "qwerty", "111111", "senha")

        return minLengthOk && hasUpper && hasLower && hasDigit && hasSpecial && noSpaces && notCommon
    }
}

class ClientePF(
    val id: Int,
    val nome: String,
    val cpf: String,
    private val senha: String
) : Autenticavel {
    init {
        require(id > 0) { "ID deve ser positivo" }
        require(nome.isNotBlank()) { "Nome não pode ser vazio" }
        require(cpf.isCpfValido()) { "CPF inválido" }
    }

    override fun autenticar(usuario: String, senha: String): Boolean {
        if (!validarSenhaForte(senha)) return false
        if (!usuario.onlyDigits().equals(cpf.onlyDigits())) return false
        return this.senha == senha
    }
}

class ClientePJ(
    val id: Int,
    val razaoSocial: String,
    val cnpj: String,
    private val senha: String
) : Autenticavel {
    init {
        require(id > 0) { "ID deve ser positivo" }
        require(razaoSocial.isNotBlank()) { "Razão social não pode ser vazia" }
        require(cnpj.matches(Regex("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"))) {
            "CNPJ inválido"
        }
        require(cnpj.isCnpjValido()) { "CNPJ inválido" }
    }

    override fun autenticar(usuario: String, senha: String): Boolean {
        if (!validarSenhaForte(senha)) return false
        if (!usuario.onlyDigits().equals(cnpj.onlyDigits())) return false
        return this.senha == senha
    }
}

/**
 * Extrai somente os dígitos numéricos de uma string.
 *
 * Utilizado para normalizar CPF e CNPJ, removendo caracteres de formatação
 * como pontos, traços e barras antes de validações ou comparações.
 *
 * @receiver String a ser processada
 * @return String contendo apenas os caracteres numéricos (0-9)
 *
 * Exemplo:
 * ```kotlin
 * "123.456.789-00".onlyDigits() // retorna "12345678900"
 * "12.345.678/0001-90".onlyDigits() // retorna "12345678000190"
 * ```
 */
private fun String.onlyDigits(): String = filter { it.isDigit() }

/**
 * Valida um CPF (Cadastro de Pessoa Física) brasileiro.
 *
 * Realiza validação completa através do algoritmo oficial de verificação:
 * - Verifica se contém exatamente 11 dígitos após normalização
 * - Rejeita sequências numéricas repetidas (ex: "111.111.111-11")
 * - Calcula e valida os dois dígitos verificadores conforme regras da Receita Federal
 *
 * O CPF pode ser fornecido com ou sem formatação (pontos e traços).
 *
 * @receiver String contendo o CPF a ser validado
 * @return `true` se o CPF é válido, `false` caso contrário
 *
 * Exemplo:
 * ```kotlin
 * "123.456.789-09".isCpfValido() // retorna true ou false conforme cálculo
 * "12345678909".isCpfValido() // aceita sem formatação
 * "111.111.111-11".isCpfValido() // retorna false (sequência repetida)
 * ```
 *
 * @see <a href="https://www.gov.br/receitafederal/pt-br">Receita Federal do Brasil</a>
 */
private fun String.isCpfValido(): Boolean {
    val digits = onlyDigits()
    if (digits.length != 11 || digits.all { it == digits.first() }) return false

    fun calcCheck(pos: Int): Int {
        val sum = digits.take(pos - 1).mapIndexed { idx, c ->
            c.digitToInt() * (pos - idx)
        }.sum()
        val mod = (sum * 10) % 11
        return if (mod == 10) 0 else mod
    }

    val check1 = calcCheck(10)
    val check2 = calcCheck(11)

    return check1 == digits[9].digitToInt() && check2 == digits[10].digitToInt()
}

/**
 * Valida um CNPJ (Cadastro Nacional de Pessoa Jurídica) brasileiro.
 *
 * Realiza validação completa através do algoritmo oficial de verificação:
 * - Verifica se contém exatamente 14 dígitos após normalização
 * - Rejeita sequências numéricas repetidas (ex: "00.000.000/0000-00")
 * - Calcula e valida os dois dígitos verificadores conforme regras da Receita Federal
 *
 * O CNPJ pode ser fornecido com ou sem formatação (pontos, barra e traços).
 *
 * @receiver String contendo o CNPJ a ser validado
 * @return `true` se o CNPJ é válido, `false` caso contrário
 *
 * Exemplo:
 * ```kotlin
 * "12.345.678/0001-95".isCnpjValido() // retorna true ou false conforme cálculo
 * "12345678000195".isCnpjValido() // aceita sem formatação
 * "00.000.000/0000-00".isCnpjValido() // retorna false (sequência repetida)
 * ```
 *
 * @see <a href="https://www.gov.br/receitafederal/pt-br">Receita Federal do Brasil</a>
 */
private fun String.isCnpjValido(): Boolean {
    val digits = onlyDigits()
    if (digits.length != 14 || digits.all { it == digits.first() }) return false

    fun calcCheck(weights: List<Int>): Int {
        val sum = digits.take(weights.size).mapIndexed { idx, c ->
            c.digitToInt() * weights[idx]
        }.sum()
        val mod = sum % 11
        return if (mod < 2) 0 else 11 - mod
    }

    val check1 = calcCheck(listOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2))
    val check2 = calcCheck(listOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2))

    return check1 == digits[12].digitToInt() && check2 == digits[13].digitToInt()
}
