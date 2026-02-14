package conta

sealed class TipoTransacao {
    data class Debito(val valor: Double) : TipoTransacao()
    data class Credito(val valor: Double) : TipoTransacao()
    data class Transferencia(val valor: Double, val contaDestino: String) : TipoTransacao()
}

class Transacao(
    val id: String,
    val tipo: TipoTransacao,
    val conta: ContaBancaria
) {
    fun executar(): Boolean = when (tipo) {
        is TipoTransacao.Debito -> conta.sacar(tipo.valor)
        is TipoTransacao.Credito -> conta.depositar(tipo.valor)
        is TipoTransacao.Transferencia ->
            if (conta.sacar(tipo.valor)) {
                println("Transferência para ${tipo.contaDestino}")
                true
            } else false
    }
}

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
