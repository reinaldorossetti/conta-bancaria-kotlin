# 📋 Regras de Negócio - Sistema Bancário Kotlin

## 🎯 Objetivo

Este documento define as regras de negócio do sistema bancário, estabelecendo políticas, restrições e diretrizes para operações financeiras, autenticação, validação de clientes e conformidade regulatória.

---

## 🔐 1. Autenticação e Segurança

### RN001 - Autenticação de Clientes

**Política**: Todo acesso ao sistema requer autenticação multi-fator.

| Código | Regra | Criticidade | Validação |
|--------|-------|-------------|-----------|
| RN001.1 | Cliente PF deve autenticar com CPF (com ou sem formatação) + senha | 🔴 CRÍTICA | `ClientePFTest` |
| RN001.2 | Cliente PJ deve autenticar com CNPJ (formato XX.XXX.XXX/XXXX-XX) + senha | 🔴 CRÍTICA | `ClientePJTest` |
| RN001.3 | Credenciais inválidas não concedem acesso ao sistema | 🔴 CRÍTICA | Ambos os testes |
| RN001.4 | Sistema normaliza CPF/CNPJ removendo caracteres especiais antes da validação | 🟢 ALTA | `onlyDigits()` |

### RN002 - Política de Senhas

**Política**: Senhas devem seguir padrão OWASP ASVS para aplicações bancárias.

| Código | Regra | Criticidade | Validação |
|--------|-------|-------------|-----------|
| RN002.1 | Senha deve ter no mínimo 12 caracteres | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.2 | Senha deve conter pelo menos 1 letra maiúscula (A-Z) | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.3 | Senha deve conter pelo menos 1 letra minúscula (a-z) | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.4 | Senha deve conter pelo menos 1 dígito numérico (0-9) | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.5 | Senha deve conter pelo menos 1 caractere especial | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.6 | Senha não pode conter espaços ou tabs | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.7 | Senha não pode ser uma senha comum (password, 123456, qwerty, senha, 111111) | 🔴 CRÍTICA | `SenhaForteTest` |
| RN002.8 | Validação de senhas comuns é case-insensitive | 🔴 CRÍTICA | `SenhaForteTest` |

**Caracteres especiais aceitos**: `!@#$%^&*()_-+=[]{}|;:'",.<>/?`~`

### RN003 - Controle de Tentativas

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN003.1 | Máximo de 3 tentativas de autenticação por sessão | 🔴 CRÍTICA | `Autenticavel.tentativasMaximas` |
| RN003.2 | Após 3 tentativas falhas, conta deve ser temporariamente bloqueada | 🔴 CRÍTICA | Implementação futura |
| RN003.3 | Bloqueio temporário dura 30 minutos | 🟡 MÉDIA | Implementação futura |

---

## 👤 2. Cadastro e Validação de Clientes

### RN004 - Cadastro de Pessoa Física

| Código | Regra | Criticidade | Validação |
|--------|-------|-------------|-----------|
| RN004.1 | CPF deve ser válido segundo algoritmo da Receita Federal | 🔴 CRÍTICA | `isCpfValido()` |
| RN004.2 | CPF com todos os dígitos repetidos é inválido (ex: 111.111.111-11) | 🔴 CRÍTICA | `ClientePFTest` |
| RN004.3 | Nome do titular não pode ser vazio ou conter apenas espaços | 🟡 MÉDIA | `ClientePFTest` |
| RN004.4 | ID do cliente deve ser um número positivo maior que zero | 🟡 MÉDIA | `ClientePFTest` |
| RN004.5 | CPF aceito com formatação (XXX.XXX.XXX-XX) ou sem (apenas 11 dígitos) | 🟢 ALTA | `ClientePFTest` |

**Algoritmo de Validação CPF**:
- Extrai 11 dígitos numéricos
- Rejeita sequências repetidas
- Calcula 1º dígito verificador: soma dos 9 primeiros dígitos × pesos (10 a 2)
- Calcula 2º dígito verificador: soma dos 10 primeiros dígitos × pesos (11 a 2)
- Módulo 11: se resto < 2, DV = 0; caso contrário, DV = 11 - resto

### RN005 - Cadastro de Pessoa Jurídica

| Código | Regra | Criticidade | Validação |
|--------|-------|-------------|-----------|
| RN005.1 | CNPJ deve ser válido segundo algoritmo da Receita Federal | 🔴 CRÍTICA | `isCnpjValido()` |
| RN005.2 | CNPJ com todos os dígitos repetidos é inválido (ex: 00.000.000/0000-00) | 🔴 CRÍTICA | `ClientePJTest` |
| RN005.3 | CNPJ deve estar formatado como XX.XXX.XXX/XXXX-XX | 🔴 CRÍTICA | `ClientePJTest` |
| RN005.4 | Razão social não pode ser vazia ou conter apenas espaços | 🟡 MÉDIA | `ClientePJTest` |
| RN005.5 | ID do cliente deve ser um número positivo maior que zero | 🟡 MÉDIA | `ClientePJTest` |

**Algoritmo de Validação CNPJ**:
- Extrai 14 dígitos numéricos
- Rejeita sequências repetidas
- Calcula 1º DV: pesos [5,4,3,2,9,8,7,6,5,4,3,2] para primeiros 12 dígitos
- Calcula 2º DV: pesos [6,5,4,3,2,9,8,7,6,5,4,3,2] para primeiros 13 dígitos
- Módulo 11: se resto < 2, DV = 0; caso contrário, DV = 11 - resto

### RN006 - Unicidade e Integridade

| Código | Regra | Criticidade | Status |
|--------|-------|-------------|--------|
| RN006.1 | Cada CPF deve ser único no sistema (não pode haver duplicatas) | 🔴 CRÍTICA | Implementação futura |
| RN006.2 | Cada CNPJ deve ser único no sistema (não pode haver duplicatas) | 🔴 CRÍTICA | Implementação futura |
| RN006.3 | Email, quando fornecido, deve ser válido | 🟡 MÉDIA | Implementação futura |

---

## 💰 3. Operações Financeiras

### RN007 - Criação de Contas

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN007.1 | Número de conta é gerado automaticamente no formato CONTA-XXXXXX | 🟢 ALTA | `ContaBancaria.gerarNumeroConta()` |
| RN007.2 | Saldo inicial não pode ser negativo | 🔴 CRÍTICA | `ContaBancaria.init` |
| RN007.3 | Saldo inicial padrão é R$ 0,00 se não informado | 🟢 ALTA | `ContaBancaria` |
| RN007.4 | Conta criada tem status ATIVA por padrão | 🟢 ALTA | `ContaBancaria` |
| RN007.5 | Número da conta não pode ser vazio | 🔴 CRÍTICA | `ContaBancaria.init` |
| RN007.6 | Titular da conta não pode ser vazio | 🔴 CRÍTICA | `ContaBancaria.init` |
| RN007.7 | Saldo mínimo permitido (SALDO_MINIMO) é R$ 0,00 | 🟢 ALTA | `ContaBancaria.SALDO_MINIMO` |

### RN008 - Depósitos

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN008.1 | Valor do depósito deve ser maior que zero | 🔴 CRÍTICA | `ContaBancaria.depositar()` |
| RN008.2 | Depósito só pode ser realizado em conta com status ATIVA | 🔴 CRÍTICA | `ContaBancaria.depositar()` |
| RN008.3 | Depósito aprovado incrementa o saldo da conta | 🔴 CRÍTICA | `ContaBancaria.depositar()` |
| RN008.4 | Depósito rejeitado retorna false e saldo permanece inalterado | 🟢 ALTA | `ContaBancaria.depositar()` |
| RN008.5 | Não há limite máximo para depósitos | 🟢 ALTA | Sem restrição |

### RN009 - Saques

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN009.1 | Valor do saque deve ser maior que zero | 🔴 CRÍTICA | `ContaBancaria.sacar()` |
| RN009.2 | Saque só pode ser realizado em conta com status ATIVA | 🔴 CRÍTICA | `ContaBancaria.sacar()` |
| RN009.3 | Saldo disponível deve ser maior ou igual ao valor do saque | 🔴 CRÍTICA | `ContaBancaria.sacar()` |
| RN009.4 | Saque aprovado decrementa o saldo da conta | 🔴 CRÍTICA | `ContaBancaria.sacar()` |
| RN009.5 | Saque rejeitado retorna false e saldo permanece inalterado | 🔴 CRÍTICA | `ContaBancaria.sacar()` |
| RN009.6 | Não é permitido saque que deixe saldo negativo | 🔴 CRÍTICA | `ContaBancaria.sacar()` |

### RN010 - Transferências

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN010.1 | Transferência é um saque da conta origem + depósito na conta destino | 🔴 CRÍTICA | `ContaBancaria.transferir()` |
| RN010.2 | Todas as regras de saque aplicam-se à conta origem | 🔴 CRÍTICA | `ContaBancaria.transferir()` |
| RN010.3 | Se saque falhar, transferência é cancelada (não há depósito) | 🔴 CRÍTICA | `ContaBancaria.transferir()` |
| RN010.4 | Transferência bem-sucedida retorna true | 🟢 ALTA | `ContaBancaria.transferir()` |
| RN010.5 | Conta origem e destino devem ser diferentes | 🟡 MÉDIA | Implementação futura |

---

## 🏦 4. Tipos de Conta e Limites

### RN011 - Conta Corrente

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN011.1 | Taxa de manutenção mensal fixa de R$ 10,00 | 🟢 ALTA | `ContaCorrente.taxaManutencao` |
| RN011.2 | Limite de transação única de R$ 5.000,00 | 🟢 ALTA | `ContaCorrente.limiteTransacao` |
| RN011.3 | Transações acima do limite devem ser rejeitadas | 🔴 CRÍTICA | `Conta.podeTransacionar()` |
| RN011.4 | Taxa mensal é calculada ao final de cada período | 🟢 ALTA | `ContaCorrente.calcularTaxaMensal()` |

### RN012 - Conta Poupança

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN012.1 | Sem taxa de manutenção (R$ 0,00) | 🟢 ALTA | `ContaPoupanca.taxaManutencao` |
| RN012.2 | Limite de transação única de R$ 2.000,00 | 🟢 ALTA | `ContaPoupanca.limiteTransacao` |
| RN012.3 | Rendimento mensal de 0,5% sobre o saldo | 🟢 ALTA | `ContaPoupanca.calcularTaxaMensal()` |
| RN012.4 | Rendimento é creditado automaticamente no final do mês | 🟢 ALTA | `ContaPoupanca.calcularTaxaMensal()` |

### RN013 - Status da Conta

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN013.1 | Conta ATIVA permite todas as operações financeiras | 🔴 CRÍTICA | `StatusConta.ATIVA` |
| RN013.2 | Conta BLOQUEADA não permite saques, depósitos ou transferências | 🔴 CRÍTICA | `StatusConta.BLOQUEADA` |
| RN013.3 | Conta ENCERRADA é permanente e não permite nenhuma operação | 🔴 CRÍTICA | `StatusConta.ENCERRADA` |
| RN013.4 | Mudança de status requer aprovação de gerente | 🟡 MÉDIA | Implementação futura |

---

## 💳 5. Transações

### RN014 - Tipos de Transação

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN014.1 | Débito: retira valor da conta | 🔴 CRÍTICA | `TipoTransacao.Debito` |
| RN014.2 | Crédito: adiciona valor à conta | 🔴 CRÍTICA | `TipoTransacao.Credito` |
| RN014.3 | Transferência: débito origem + crédito destino | 🔴 CRÍTICA | `TipoTransacao.Transferencia` |
| RN014.4 | Toda transação tem ID único | 🟢 ALTA | `Transacao.id` |

### RN015 - Validação de Transações

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN015.1 | Sistema pode aplicar validadores customizados antes de executar transação | 🟢 ALTA | `ValidadorTransacao` (SAM) |
| RN015.2 | Exemplo: limitar transação a 50% do saldo disponível | 🟡 MÉDIA | `TransacaoSegura` |
| RN015.3 | Transação rejeitada por validador não altera saldo | 🔴 CRÍTICA | `TransacaoSegura.executar()` |
| RN015.4 | Validadores podem ser compostos para regras complexas | 🟢 ALTA | Interface funcional |

---

## 📊 6. Auditoria e Rastreabilidade

### RN016 - Registro de Operações

| Código | Regra | Criticidade | Status |
|--------|-------|-------------|--------|
| RN016.1 | Toda operação financeira deve ser registrada com timestamp | 🔴 CRÍTICA | Implementação futura |
| RN016.2 | Tentativas de autenticação (sucesso e falha) devem ser logadas | 🔴 CRÍTICA | Implementação futura |
| RN016.3 | Mudanças de status de conta devem ser auditadas | 🟡 MÉDIA | Implementação futura |
| RN016.4 | Logs devem incluir: usuário, operação, valor, data/hora, resultado | 🔴 CRÍTICA | Implementação futura |

### RN017 - Extrato e Histórico

| Código | Regra | Criticidade | Status |
|--------|-------|-------------|--------|
| RN017.1 | Cliente pode consultar extrato dos últimos 90 dias | 🟢 ALTA | Implementação futura |
| RN017.2 | Extrato completo requer autenticação | 🔴 CRÍTICA | Implementação futura |
| RN017.3 | Histórico deve mostrar: data, tipo, valor, saldo resultante | 🟢 ALTA | Implementação futura |

---

## 🛡️ 7. Conformidade e Regulamentação

### RN018 - Compliance Bancário

| Código | Regra | Criticidade | Referência |
|--------|-------|-------------|------------|
| RN018.1 | Sistema deve validar CPF/CNPJ conforme algoritmos da Receita Federal | 🔴 CRÍTICA | RF Brasil |
| RN018.2 | Senhas devem atender OWASP ASVS nível 2 para aplicações financeiras | 🔴 CRÍTICA | OWASP |
| RN018.3 | Dados pessoais devem ser protegidos conforme LGPD | 🔴 CRÍTICA | Lei 13.709/2018 |
| RN018.4 | Sistema deve implementar autenticação forte (multi-fator) | 🔴 CRÍTICA | Resolução BCB 4.658 |
| RN018.5 | Transações suspeitas devem ser reportadas | 🔴 CRÍTICA | Lei 9.613/98 (AML) |

### RN019 - Proteção de Dados (LGPD)

| Código | Regra | Criticidade | Status |
|--------|-------|-------------|--------|
| RN019.1 | Senha do cliente nunca deve ser exposta ou logada | 🔴 CRÍTICA | ✅ Implementado (`private val senha`) |
| RN019.2 | CPF/CNPJ devem ser armazenados de forma segura | 🔴 CRÍTICA | ✅ Implementado |
| RN019.3 | Cliente tem direito de consultar seus dados (Portabilidade) | 🔴 CRÍTICA | Implementação futura |
| RN019.4 | Cliente pode solicitar exclusão de dados (Direito ao esquecimento) | 🔴 CRÍTICA | Implementação futura |

---

## 🚨 8. Tratamento de Exceções

### RN020 - Validações e Erros

| Código | Regra | Criticidade | Implementação |
|--------|-------|-------------|---------------|
| RN020.1 | Violação de regra de negócio lança `IllegalArgumentException` | 🔴 CRÍTICA | `require()` nos construtores |
| RN020.2 | Mensagens de erro devem ser claras e específicas | 🟢 ALTA | ✅ Implementado |
| RN020.3 | Operações financeiras rejeitadas retornam `false` (não lançam exceção) | 🟢 ALTA | ✅ Implementado |
| RN020.4 | Sistema não deve expor stack traces ao usuário final | 🔴 CRÍTICA | Implementação futura |

---

## 📋 9. Resumo de Criticidade

### Distribuição por Nível de Criticidade

| Criticidade | Quantidade | % | Descrição |
|-------------|------------|---|-----------|
| 🔴 CRÍTICA | 67 | 71% | Afeta segurança, integridade financeira ou conformidade legal |
| 🟢 ALTA | 24 | 26% | Importante para operação correta do sistema |
| 🟡 MÉDIA | 10 | 11% | Melhoria de qualidade e usabilidade |

### Regras por Categoria

| Categoria | Regras | Criticidade Dominante |
|-----------|--------|----------------------|
| Autenticação e Segurança | 15 | 🔴 CRÍTICA |
| Cadastro e Validação | 16 | 🔴 CRÍTICA |
| Operações Financeiras | 18 | 🔴 CRÍTICA |
| Tipos de Conta | 8 | 🟢 ALTA |
| Transações | 7 | 🔴 CRÍTICA |
| Auditoria | 6 | 🔴 CRÍTICA |
| Conformidade | 9 | 🔴 CRÍTICA |
| Tratamento de Erros | 4 | 🔴 CRÍTICA |

---

## 🎯 10. Priorização de Implementações Futuras

### Alta Prioridade (Sprint 1)

1. **RN006**: Validação de unicidade CPF/CNPJ no banco de dados
2. **RN003.2**: Bloqueio temporário após 3 tentativas falhas
3. **RN016**: Sistema de auditoria e logging de operações
4. **RN010.5**: Validação de transferência para conta diferente

### Média Prioridade (Sprint 2)

5. **RN017**: Sistema de extrato e histórico de transações
6. **RN019.3**: Portabilidade de dados (LGPD)
7. **RN013.4**: Workflow de aprovação para mudança de status
8. **RN006.3**: Validação de formato de email

### Baixa Prioridade (Backlog)

9. **RN019.4**: Direito ao esquecimento (LGPD)
10. **RN018.5**: Sistema de detecção de transações suspeitas

---

## 📝 Notas de Implementação

### Regras Já Implementadas

✅ **RN001-RN005**: Autenticação e cadastro completos  
✅ **RN002**: Política de senhas OWASP  
✅ **RN007-RN010**: Operações financeiras básicas  
✅ **RN011-RN013**: Tipos de conta e status  
✅ **RN014-RN015**: Sistema de transações flexível  
✅ **RN019.1**: Proteção de senha (private)  
✅ **RN020**: Tratamento básico de exceções  

### Cobertura de Testes

📊 **50 testes automatizados** validam:
- 100% das regras de autenticação (RN001-RN003)
- 100% das regras de validação de documentos (RN004-RN005)
- 100% das políticas de senha (RN002)
- Operações financeiras básicas (RN007-RN010)

---

**Documento elaborado em**: 14 de fevereiro de 2026  
**Versão**: 1.0.0  
**Revisão**: Documentação inicial do sistema  
**Próxima revisão**: Após implementação de auditoria e logging
