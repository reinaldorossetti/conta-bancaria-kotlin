package conta

/**
 * Demonstração de uso do sistema bancário com autenticação.
 */
fun main() {
    println("=== Sistema Bancário - Demonstração ===\n")

    // Exemplo 1: Cliente Pessoa Física
    println("1. Criando Cliente Pessoa Física:")
    try {
        val clientePF = ClientePF(
            id = 1,
            nome = "João da Silva",
            cpf = "123.456.789-09",
            senha = "Senh@Segura123"
        )
        println("✓ Cliente PF criado: ${clientePF.nome} (CPF: ${clientePF.cpf})")
        
        // Testando autenticação
        println("\n2. Testando autenticação do Cliente PF:")
        val autenticado1 = clientePF.autenticar("123.456.789-09", "Senh@Segura123")
        println("   Autenticação com credenciais corretas: ${if (autenticado1) "✓ Sucesso" else "✗ Falhou"}")
        
        val autenticado2 = clientePF.autenticar("123.456.789-09", "senhaerrada")
        println("   Autenticação com senha incorreta: ${if (!autenticado2) "✓ Rejeitado corretamente" else "✗ Erro"}")
        
    } catch (e: IllegalArgumentException) {
        println("✗ Erro ao criar cliente: ${e.message}")
    }

    // Exemplo 2: Cliente Pessoa Jurídica
    println("\n3. Criando Cliente Pessoa Jurídica:")
    try {
        val clientePJ = ClientePJ(
            id = 1,
            razaoSocial = "Tech Solutions Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Empres@Segura456"
        )
        println("✓ Cliente PJ criado: ${clientePJ.razaoSocial} (CNPJ: ${clientePJ.cnpj})")
        
        // Testando autenticação
        println("\n4. Testando autenticação do Cliente PJ:")
        val autenticado3 = clientePJ.autenticar("11222333000181", "Empres@Segura456")
        println("   Autenticação com CNPJ sem formatação: ${if (autenticado3) "✓ Sucesso" else "✗ Falhou"}")
        
    } catch (e: IllegalArgumentException) {
        println("✗ Erro ao criar cliente: ${e.message}")
    }

    // Exemplo 3: Tentativa com CPF inválido
    println("\n5. Tentando criar cliente com CPF inválido:")
    try {
        ClientePF(
            id = 2,
            nome = "Maria Santos",
            cpf = "111.111.111-11",
            senha = "Senh@Forte789"
        )
        println("✗ Erro: Deveria ter rejeitado CPF inválido")
    } catch (e: IllegalArgumentException) {
        println("✓ CPF inválido rejeitado corretamente: ${e.message}")
    }

    // Exemplo 4: Testando validação de senha fraca
    println("\n6. Testando validação de senha:")
    val cliente = ClientePF(
        id = 3,
        nome = "Pedro Oliveira",
        cpf = "987.654.321-00",
        senha = "S3nh@Fort3Valid@"
    )
    
    println("   Senha forte válida: ${if (cliente.validarSenhaForte("S3nh@Fort3Valid@")) "✓ Aceita" else "✗ Rejeitada"}")
    println("   Senha curta: ${if (!cliente.validarSenhaForte("Sen@1")) "✓ Rejeitada" else "✗ Aceita"}")
    println("   Senha sem especial: ${if (!cliente.validarSenhaForte("SenhaForte123")) "✓ Rejeitada" else "✗ Aceita"}")
    println("   Senha comum 'password': ${if (!cliente.validarSenhaForte("Password@123")) "✓ Rejeitada" else "✗ Aceita"}")

    // Exemplo 5: Criando conta bancária para cliente
    println("\n7. Criando conta bancária para cliente:")
    val contaPF = ContaBancaria.criar("João da Silva", 1000.0)
    println("✓ Conta criada: ${contaPF.numero} - Titular: ${contaPF.titular}")
    println("   Saldo inicial: R$ %.2f".format(contaPF.saldo))
    
    contaPF.depositar(500.0)
    println("   Após depósito de R$ 500: R$ %.2f".format(contaPF.saldo))
    
    contaPF.sacar(200.0)
    println("   Após saque de R$ 200: R$ %.2f".format(contaPF.saldo))

    println("\n=== Demonstração concluída ===")
}
