package conta

abstract class Conta(
    val numero: String,
    val titular: String,
    var saldo: Double
) {
    abstract val taxaManutencao: Double
    abstract val limiteTransacao: Double
    abstract fun calcularTaxaMensal(): Double

    fun podeTransacionar(valor: Double): Boolean = valor <= limiteTransacao

    fun depositar(valor: Double): Boolean {
        if (valor <= 0) return false
        saldo += valor
        return true
    }
}

class ContaCorrente(numero: String, titular: String, saldo: Double) :
    Conta(numero, titular, saldo) {
    override val taxaManutencao: Double = 10.0
    override val limiteTransacao: Double = 5_000.0
    override fun calcularTaxaMensal(): Double = taxaManutencao
}

class ContaPoupanca(numero: String, titular: String, saldo: Double) :
    Conta(numero, titular, saldo) {
    override val taxaManutencao: Double = 0.0
    override val limiteTransacao: Double = 2_000.0
    override fun calcularTaxaMensal(): Double = saldo * 0.005
}
