package conta

class ContaBancaria(
    val numero: String,
    val titular: String,
    saldoInicial: Double = 0.0,
    var status: StatusConta = StatusConta.ATIVA
) {
    var saldo: Double = saldoInicial
        private set

    init {
        require(numero.isNotBlank()) { "Número da conta não pode ser vazio" }
        require(titular.isNotBlank()) { "Titular não pode ser vazio" }
        require(saldoInicial >= SALDO_MINIMO) { "Saldo inicial deve ser positivo ou zero" }
    }

    fun depositar(valor: Double): Boolean {
        if (valor <= 0 || status != StatusConta.ATIVA) return false
        saldo += valor
        return true
    }

    fun sacar(valor: Double): Boolean {
        if (valor <= 0 || status != StatusConta.ATIVA) return false
        if (saldo < valor) return false
        saldo -= valor
        return true
    }

    fun transferir(destino: ContaBancaria, valor: Double): Boolean {
        if (!sacar(valor)) return false
        destino.depositar(valor)
        return true
    }

    companion object {
        const val SALDO_MINIMO = 0.0
        private var contadorContas = 0

        fun gerarNumeroConta(): String {
            contadorContas += 1
            return "CONTA-${contadorContas.toString().padStart(6, '0')}"
        }

        fun criar(titular: String, saldoInicial: Double = 0.0): ContaBancaria =
            ContaBancaria(gerarNumeroConta(), titular, saldoInicial)
    }
}
