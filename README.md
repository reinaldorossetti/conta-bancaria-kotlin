🌟 Introdução à Linguagem Kotlin
================================

Kotlin é uma linguagem moderna da JetBrains que roda na JVM, interoperável com Java e hoje preferida pelo Google para Android.

🚀 Novidades Recentes do Kotlin
-------------------------------

- ⚡ **K2 Compiler:** compilação mais rápida e análise de tipos aprimorada.
- 🧊 **Data objects:** singletons com benefícios de `data class`.
- 🧮 **Inline value classes:** otimização sem overhead de objeto.
- 🧭 **Context receivers:** DSLs mais limpas.
- 🌊 **Coroutines melhoradas:** suporte assíncrono mais robusto.

✅ Principais Vantagens do Uso de Kotlin
---------------------------------------

### 🧹 Sintaxe Concisa e Legível
- Menos boilerplate; leitura e manutenção simplificadas.
- Use funções de escopo para configuração fluida:

```kotlin
val conta = ContaBancaria("123-4", "João", 1000.0).apply {
    depositar(500.0)
    sacar(200.0)
}
```

### 🛡️ Segurança contra Null Pointer Exceptions
- Null safety embutido exige declarar explicitamente valores anuláveis.

```kotlin
var titular: String = "João"
var email: String? = null

val tamanho = email?.length ?: 0
email?.let { println("Email: $it") }
val emailValido = email ?: "sem@email.com"
```

### 🤝 Interoperabilidade com Java
- Integração total com código Java, útil para legados e migrações graduais.
- Kotlin 2.0 melhora geração Java-friendly.

### ⚡ Produtividade Elevada
- `data class`, funções de extensão e lambdas reduzem complexidade.

```kotlin
fun Double.formatarMoeda(): String = "R$ %.2f".format(this)

val saldo = 1500.50
println(saldo.formatarMoeda())
```

### 📱 Suporte Oficial para Android
- Linguagem principal indicada pelo Google; documentação e comunidade fortes.
- Kotlin Multiplatform compartilha código entre Android, iOS, Web e Desktop.

🏗️ Construtores Primário e Secundário
-------------------------------------

### 🔹 Construtor Primário

```kotlin
class ContaBancaria(val numero: String, val titular: String, var saldo: Double)
```

- `val`/`var` promovem parâmetros a propriedades.
- Blocos `init` rodam na inicialização:

```kotlin
class ContaBancaria(val numero: String, val titular: String, var saldo: Double) {
    init {
        require(saldo >= 0) { "Saldo inicial não pode ser negativo" }
        println("Conta $numero criada para $titular com saldo R$$saldo")
    }
}
```

### 🔹 Construtor Secundário

```kotlin
class ContaBancaria(val numero: String, val titular: String, var saldo: Double) {
    constructor(numero: String, titular: String) : this(numero, titular, 0.0) {
        println("Conta criada com saldo inicial zerado para $titular")
    }
}
```

- Prefira valores padrão para reduzir sobrecarga de construtores:

```kotlin
class ContaBancaria(
    val numero: String,
    val titular: String,
    var saldo: Double = 0.0,
    val tipo: String = "CORRENTE"
)
```

🧭 Entendendo o `this` em Construtores
-------------------------------------

- Diferencia propriedades de parâmetros com mesmo nome.

```kotlin
class ContaBancaria(numero: String, titular: String) {
    val numero: String
    val titular: String

    init {
        this.numero = numero
        this.titular = titular
    }
}
```

- Delegar para outro construtor: `this(...)`.
- Passar a própria instância como argumento em métodos auxiliares.

🧬 Herança em Kotlin
--------------------

```kotlin
open class Conta(val numero: String, val titular: String, var saldo: Double) {
    fun depositar(valor: Double) { if (valor > 0) saldo += valor }
    open fun calcularTaxa(): Double = 0.0
}

class ContaPoupanca(numero: String, titular: String, saldo: Double) : Conta(numero, titular, saldo) {
    override fun calcularTaxa(): Double = saldo * 0.005
}
```

- `open` habilita herança; `override` redefine comportamento.
- `sealed class` restringe hierarquias, permitindo `when` exaustivo.

🎭 Polimorfismo
---------------

```kotlin
val contas: List<Conta> = listOf(
    Conta("123-4", "João", 2000.0),
    ContaPoupanca("123-5", "Maria", 1000.0)
)

for (conta in contas) {
    println("Conta ${conta.numero} - Taxa: R$${conta.calcularTaxa()}")
}
```

- O método executado depende da instância concreta, mesmo em coleções do tipo base.

🏦 Modelagem Bancária: Classes e Uso
-----------------------------------

### 🔹 Classe Básica: Conta Bancária

```kotlin
class ContaBancaria(val numero: String, val titular: String, var saldo: Double) {
    fun depositar(valor: Double) {
        if (valor > 0) {
            saldo += valor
            println("Depósito de R$$valor. Saldo: R$$saldo")
        }
    }

    fun sacar(valor: Double): Boolean =
        if (valor > 0 && saldo >= valor) {
            saldo -= valor
            println("Saque de R$$valor. Saldo: R$$saldo")
            true
        } else {
            println("Saque não realizado")
            false
        }
}
```

### 🧩 Enum para Status da Conta

```kotlin
enum class StatusConta { ATIVA, BLOQUEADA, ENCERRADA }

class ContaComStatus(
    val numero: String,
    val titular: String,
    var saldo: Double,
    var status: StatusConta = StatusConta.ATIVA
) {
    fun sacar(valor: Double): Boolean {
        if (status != StatusConta.ATIVA) return false
        return if (valor > 0 && saldo >= valor) { saldo -= valor; true } else false
    }
}
```

### 🧾 `data class` para Cliente

```kotlin
data class Cliente(
    val id: Int,
    val nome: String,
    val cpf: String,
    val email: String?
) {
    init {
        require(id > 0)
        require(nome.isNotBlank())
        require(cpf.matches(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")))
    }
}

val cliente1 = Cliente(1, "João Silva", "123.456.789-00", "joao@email.com")
val (id, nome, cpf, email) = cliente1
```

### 💳 Transações: Débito e Crédito

```kotlin
sealed class TipoTransacao {
    data class Debito(val valor: Double) : TipoTransacao()
    data class Credito(val valor: Double) : TipoTransacao()
    data class Transferencia(val valor: Double, val contaDestino: String) : TipoTransacao()
}

class Transacao(val id: String, val tipo: TipoTransacao, val conta: ContaBancaria) {
    fun executar(): Boolean = when (tipo) {
        is TipoTransacao.Debito -> conta.sacar(tipo.valor)
        is TipoTransacao.Credito -> { conta.depositar(tipo.valor); true }
        is TipoTransacao.Transferencia ->
            if (conta.sacar(tipo.valor)) { println("Transferência para ${tipo.contaDestino}"); true } else false
    }
}
```

### 🧱 Classes Abstratas para Tipos de Conta

```kotlin
abstract class Conta(val numero: String, val titular: String, var saldo: Double) {
    abstract val taxaManutencao: Double
    abstract val limiteTransacao: Double
    abstract fun calcularTaxaMensal(): Double

    fun podeTransacionar(valor: Double) = valor <= limiteTransacao
    fun depositar(valor: Double) { if (valor > 0) saldo += valor }
}

class ContaCorrente(numero: String, titular: String, saldo: Double) : Conta(numero, titular, saldo) {
    override val taxaManutencao = 10.0
    override val limiteTransacao = 5000.0
    override fun calcularTaxaMensal() = taxaManutencao
}

class ContaPoupanca(numero: String, titular: String, saldo: Double) : Conta(numero, titular, saldo) {
    override val taxaManutencao = 0.0
    override val limiteTransacao = 2000.0
    override fun calcularTaxaMensal() = saldo * 0.005
}
```

### 🔐 Interfaces para Autenticação

```kotlin
interface Autenticavel {
    val tentativasMaximas: Int get() = 3
    fun autenticar(senha: String): Boolean

    fun validarSenhaForte(senha: String): Boolean =
        senha.length >= 8 && senha.any { it.isDigit() } && senha.any { it.isUpperCase() }
}

class ClientePF(
    val id: Int,
    val nome: String,
    val cpf: String,
    private val senha: String
) : Autenticavel {
    override fun autenticar(senha: String): Boolean {
        if (!validarSenhaForte(senha)) return false
        return this.senha == senha
    }
}
```

🚀 Recursos Avançados e Boas Práticas
-------------------------------------

### 🔧 Extension Functions

```kotlin
fun ContaBancaria.transferir(destino: ContaBancaria, valor: Double): Boolean =
    if (sacar(valor)) {
        destino.depositar(valor)
        println("Transferência de R$$valor para ${destino.numero}")
        true
    } else false
```

### 🧭 Scope Functions

```kotlin
val conta = ContaBancaria("123-4", "João", 0.0).apply {
    depositar(1000.0)
    sacar(200.0)
}

val saldoFinal = conta.run {
    depositar(500.0)
    sacar(100.0)
    saldo
}

val email: String? = "user@email.com"
email?.let { println("Notificando $it") }
```

### 🤝 Companion Objects

```kotlin
class ContaBancaria(val numero: String, val titular: String, var saldo: Double) {
    companion object {
        const val SALDO_MINIMO = 0.0
        private var contadorContas = 0

        fun gerarNumeroConta(): String {
            contadorContas++
            return "CONTA-${contadorContas.toString().padStart(6, '0')}"
        }

        fun criar(titular: String, saldoInicial: Double = 0.0): ContaBancaria =
            ContaBancaria(gerarNumeroConta(), titular, saldoInicial)
    }
}
```

### 🛰️ Delegação de Propriedades

```kotlin
import kotlin.properties.Delegates

class ContaObservavel(val numero: String, val titular: String, saldoInicial: Double) {
    var saldo: Double by Delegates.observable(saldoInicial) { _, old, new ->
        println("Saldo alterado de R$$old para R$$new")
    }

    val extrato: List<String> by lazy {
        println("Carregando extrato...")
        listOf("Transação 1", "Transação 2")
    }
}
```

### 🧠 Interfaces Funcionais (SAM)

```kotlin
fun interface ValidadorTransacao {
    fun validar(valor: Double, saldo: Double): Boolean
}

class TransacaoSegura(
    val valor: Double,
    val conta: ContaBancaria,
    val validador: ValidadorTransacao
) {
    fun executar(): Boolean = if (validador.validar(valor, conta.saldo)) conta.sacar(valor) else false
}

val transacao = TransacaoSegura(100.0, conta) { valor, saldo -> valor <= saldo * 0.5 }
```

🧠 Considerações Finais
-----------------------

- Kotlin é concisa, segura e interoperável, ideal para Android e backend.
- Prefira imutabilidade (`val`) e null safety para evitar falhas.
- Use `data class` para modelos, `sealed class` para hierarquias restritas.
- Extensões, scope functions e companion objects mantêm código enxuto.
- Coroutines oferecem assincronia moderna sem complexidade de threads.

🆕 Recursos Modernos para Explorar
----------------------------------

- 🌍 Kotlin Multiplatform: compartilhamento de código entre plataformas.
- 🖥️ Compose Multiplatform: UI declarativa multiplataforma.
- 🧭 Context receivers: DSLs e APIs fluentes simplificadas.
- 🧮 Inline value classes: performance sem overhead de objeto.
- 🧠 Melhorias de smart cast no Kotlin 2.0.

Se quiser, posso montar um exemplo integrado de sistema bancário completo em Kotlin aplicando todos esses conceitos.

---

## 🧪 Estrutura do Projeto

```
conta-bancaria-kotlin/
├── src/
│   ├── main/kotlin/conta/
│   │   ├── Autenticavel.kt      # Interface e validações de autenticação
│   │   ├── Cliente.kt           # Data class Cliente
│   │   ├── Conta.kt             # Classes abstratas e herança
│   │   ├── ContaBancaria.kt     # Classe principal conta bancária
│   │   ├── StatusConta.kt       # Enum de status
│   │   └── Transacao.kt         # Sistema de transações
│   ├── test/kotlin/conta/
│   │   ├── ClientePFTest.kt     # Testes Cliente PF
│   │   ├── ClientePJTest.kt     # Testes Cliente PJ
│   │   └── SenhaForteTest.kt    # Testes validação senha
│   └── Main.kt                  # Exemplo de uso
├── build.gradle.kts
├── settings.gradle.kts
├── junit-testing.MD             # 📊 Relatório detalhado de testes
├── REGRAS-DE-NEGOCIO.md         # 📋 Regras de negócio do sistema
└── README.md
```

## 🚀 Como Executar

### Pré-requisitos
- JDK 17 ou superior
- Gradle 8.x

### Rodar o exemplo
```bash
./gradlew run
```

### Executar testes
```bash
./gradlew test
```

### Compilar o projeto
```bash
./gradlew build
```

## 🧪 Cobertura de Testes

Os testes cobrem:

✅ **ClientePF**: Validação de CPF, criação, autenticação  
✅ **ClientePJ**: Validação de CNPJ, criação, autenticação  
✅ **Senha Forte**: Todos os critérios OWASP (12+ chars, maiúscula, minúscula, dígito, especial, sem espaços, não comum)  
✅ **Edge Cases**: IDs inválidos, documentos repetidos, formatos com/sem máscara

## 📚 Documentação Adicional

- 📊 **[junit-testing.MD](junit-testing.MD)** - Relatório completo de testes com 50 casos, estatísticas, criticidade e métricas de qualidade
- 📋 **[REGRAS-DE-NEGOCIO.md](REGRAS-DE-NEGOCIO.md)** - 94 regras de negócio documentadas cobrindo autenticação, operações financeiras, conformidade e segurança