package conta

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Testes unitários para a classe ClientePF.
 * 
 * Valida criação de cliente, validação de CPF e autenticação com senha forte.
 */
class ClientePFTest {

    @Test
    fun `deve criar cliente PF com CPF valido formatado`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertEquals(1, cliente.id)
        assertEquals("João Silva", cliente.nome)
        assertEquals("123.456.789-09", cliente.cpf)
    }

    @Test
    fun `deve criar cliente PF com CPF valido sem formatacao`() {
        val cliente = ClientePF(
            id = 2,
            nome = "Maria Santos",
            cpf = "12345678909",
            senha = "OutraSenha@456"
        )
        
        assertEquals(2, cliente.id)
        assertEquals("Maria Santos", cliente.nome)
    }

    @Test
    fun `deve rejeitar criacao com ID invalido`() {
        val exception = assertThrows<IllegalArgumentException> {
            ClientePF(
                id = 0,
                nome = "João Silva",
                cpf = "123.456.789-09",
                senha = "Senha@Forte123"
            )
        }
        
        assertTrue(exception.message!!.contains("ID deve ser positivo"))
    }

    @Test
    fun `deve rejeitar criacao com nome vazio`() {
        val exception = assertThrows<IllegalArgumentException> {
            ClientePF(
                id = 1,
                nome = "",
                cpf = "123.456.789-09",
                senha = "Senha@Forte123"
            )
        }
        
        assertTrue(exception.message!!.contains("Nome não pode ser vazio"))
    }

    @Test
    fun `deve rejeitar criacao com CPF invalido`() {
        assertThrows<IllegalArgumentException> {
            ClientePF(
                id = 1,
                nome = "João Silva",
                cpf = "123.456.789-00",
                senha = "Senha@Forte123"
            )
        }
    }

    @Test
    fun `deve rejeitar criacao com CPF de numeros repetidos`() {
        assertThrows<IllegalArgumentException> {
            ClientePF(
                id = 1,
                nome = "João Silva",
                cpf = "111.111.111-11",
                senha = "Senha@Forte123"
            )
        }
    }

    @Test
    fun `deve autenticar com CPF e senha corretos formatados`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertTrue(cliente.autenticar("123.456.789-09", "Senha@Forte123"))
    }

    @Test
    fun `deve autenticar com CPF sem formatacao`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertTrue(cliente.autenticar("12345678909", "Senha@Forte123"))
    }

    @Test
    fun `deve rejeitar autenticacao com CPF incorreto`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("987.654.321-00", "Senha@Forte123"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha incorreta`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("123.456.789-09", "SenhaErrada@456"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha fraca`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("123.456.789-09", "senha123"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha curta`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("123.456.789-09", "Sen@1Ab"))
    }

    @Test
    fun `deve respeitar propriedade tentativasMaximas`() {
        val cliente = ClientePF(
            id = 1,
            nome = "João Silva",
            cpf = "123.456.789-09",
            senha = "Senha@Forte123"
        )
        
        assertEquals(3, cliente.tentativasMaximas)
    }
}
