package conta

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Testes unitários para a classe ClientePJ.
 * 
 * Valida criação de cliente, validação de CNPJ e autenticação com senha forte.
 */
class ClientePJTest {

    @Test
    fun `deve criar cliente PJ com CNPJ valido formatado`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertEquals(1, cliente.id)
        assertEquals("Empresa ABC Ltda", cliente.razaoSocial)
        assertEquals("11.222.333/0001-81", cliente.cnpj)
    }

    @Test
    fun `deve criar cliente PJ com outro CNPJ valido`() {
        val cliente = ClientePJ(
            id = 2,
            razaoSocial = "Tech Solutions SA",
            cnpj = "22.333.444/0001-07",
            senha = "OutraSenha@456"
        )
        
        assertEquals(2, cliente.id)
        assertEquals("Tech Solutions SA", cliente.razaoSocial)
    }

    @Test
    fun `deve rejeitar criacao com ID invalido`() {
        val exception = assertThrows<IllegalArgumentException> {
            ClientePJ(
                id = -1,
                razaoSocial = "Empresa ABC Ltda",
                cnpj = "11.222.333/0001-81",
                senha = "Senha@Forte123"
            )
        }
        
        assertTrue(exception.message!!.contains("ID deve ser positivo"))
    }

    @Test
    fun `deve rejeitar criacao com razao social vazia`() {
        val exception = assertThrows<IllegalArgumentException> {
            ClientePJ(
                id = 1,
                razaoSocial = "   ",
                cnpj = "11.222.333/0001-81",
                senha = "Senha@Forte123"
            )
        }
        
        assertTrue(exception.message!!.contains("Razão social não pode ser vazia"))
    }

    @Test
    fun `deve rejeitar criacao com CNPJ sem formatacao`() {
        val exception = assertThrows<IllegalArgumentException> {
            ClientePJ(
                id = 1,
                razaoSocial = "Empresa ABC Ltda",
                cnpj = "11222333000181",  // Sem formatação
                senha = "Senha@Forte123"
            )
        }
        
        assertTrue(exception.message!!.contains("CNPJ inválido"))
    }

    @Test
    fun `deve rejeitar criacao com CNPJ invalido`() {
        assertThrows<IllegalArgumentException> {
            ClientePJ(
                id = 1,
                razaoSocial = "Empresa ABC Ltda",
                cnpj = "11.222.333/0001-00",
                senha = "Senha@Forte123"
            )
        }
    }

    @Test
    fun `deve rejeitar criacao com CNPJ de numeros repetidos`() {
        assertThrows<IllegalArgumentException> {
            ClientePJ(
                id = 1,
                razaoSocial = "Empresa ABC Ltda",
                cnpj = "00.000.000/0000-00",
                senha = "Senha@Forte123"
            )
        }
    }

    @Test
    fun `deve autenticar com CNPJ e senha corretos formatados`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertTrue(cliente.autenticar("11.222.333/0001-81", "Senha@Forte123"))
    }

    @Test
    fun `deve autenticar com CNPJ sem formatacao`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertTrue(cliente.autenticar("11222333000181", "Senha@Forte123"))
    }

    @Test
    fun `deve rejeitar autenticacao com CNPJ incorreto`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("99.888.777/0001-00", "Senha@Forte123"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha incorreta`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("11.222.333/0001-81", "SenhaErrada@456"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha fraca sem caractere especial`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("11.222.333/0001-81", "SenhaFraca123"))
    }

    @Test
    fun `deve rejeitar autenticacao com senha comum`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertFalse(cliente.autenticar("11.222.333/0001-81", "Password@123"))
    }

    @Test
    fun `deve respeitar propriedade tentativasMaximas`() {
        val cliente = ClientePJ(
            id = 1,
            razaoSocial = "Empresa ABC Ltda",
            cnpj = "11.222.333/0001-81",
            senha = "Senha@Forte123"
        )
        
        assertEquals(3, cliente.tentativasMaximas)
    }
}
