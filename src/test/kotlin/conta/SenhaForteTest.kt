package conta

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Testes unitários para validação de senha forte.
 * 
 * Testa todos os critérios de segurança baseados em OWASP ASVS.
 */
class SenhaForteTest {

    private val clienteTeste = ClientePF(
        id = 1,
        nome = "Teste",
        cpf = "123.456.789-09",
        senha = "SenhaValida@123"
    )

    @Test
    fun `deve aceitar senha forte valida`() {
        assertTrue(clienteTeste.validarSenhaForte("Senha@Forte123"))
    }

    @Test
    fun `deve aceitar senha com todos os requisitos minimos`() {
        assertTrue(clienteTeste.validarSenhaForte("Aa1@bcdefghij"))
    }

    @Test
    fun `deve aceitar senha com multiplos caracteres especiais`() {
        assertTrue(clienteTeste.validarSenhaForte("S3nh@F0rt3#Compl3x@"))
    }

    @Test
    fun `deve rejeitar senha com menos de 12 caracteres`() {
        assertFalse(clienteTeste.validarSenhaForte("Senh@123"))
    }

    @Test
    fun `deve rejeitar senha exatamente com 11 caracteres`() {
        assertFalse(clienteTeste.validarSenhaForte("Senha@Fort1"))
    }

    @Test
    fun `deve rejeitar senha sem letra maiuscula`() {
        assertFalse(clienteTeste.validarSenhaForte("senha@forte123"))
    }

    @Test
    fun `deve rejeitar senha sem letra minuscula`() {
        assertFalse(clienteTeste.validarSenhaForte("SENHA@FORTE123"))
    }

    @Test
    fun `deve rejeitar senha sem digito`() {
        assertFalse(clienteTeste.validarSenhaForte("Senha@Forte!!!"))
    }

    @Test
    fun `deve rejeitar senha sem caractere especial`() {
        assertFalse(clienteTeste.validarSenhaForte("SenhaForte12345"))
    }

    @Test
    fun `deve rejeitar senha com espacos`() {
        assertFalse(clienteTeste.validarSenhaForte("Senha @Forte 123"))
    }

    @Test
    fun `deve rejeitar senha com tabs`() {
        assertFalse(clienteTeste.validarSenhaForte("Senha@Forte\t123"))
    }

    @Test
    fun `deve rejeitar senha comum password`() {
        assertFalse(clienteTeste.validarSenhaForte("Password@123456"))
    }

    @Test
    fun `deve rejeitar senha comum 123456`() {
        assertFalse(clienteTeste.validarSenhaForte("123456@Senha!"))
    }

    @Test
    fun `deve rejeitar senha comum qwerty`() {
        assertFalse(clienteTeste.validarSenhaForte("Qwerty@12345"))
    }

    @Test
    fun `deve rejeitar senha comum senha`() {
        assertFalse(clienteTeste.validarSenhaForte("Senha@123456789"))
    }

    @Test
    fun `deve rejeitar senha comum 111111`() {
        assertFalse(clienteTeste.validarSenhaForte("111111@SenhaA"))
    }

    @Test
    fun `deve validar senha case-insensitive para palavras comuns`() {
        assertFalse(clienteTeste.validarSenhaForte("PASSWORD@123456"))
        assertFalse(clienteTeste.validarSenhaForte("PaSsWoRd@123456"))
    }

    @Test
    fun `deve aceitar senha longa com todos os requisitos`() {
        assertTrue(clienteTeste.validarSenhaForte("M1nh@S3nh@Sup3rS3gur@2026!"))
    }

    @Test
    fun `deve validar diferentes caracteres especiais`() {
        assertTrue(clienteTeste.validarSenhaForte("Senha!123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha@123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha#123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha\$123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha%123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha^123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha&123Forte"))
        assertTrue(clienteTeste.validarSenhaForte("Senha*123Forte"))
    }

    @Test
    fun `deve rejeitar senha vazia`() {
        assertFalse(clienteTeste.validarSenhaForte(""))
    }

    @Test
    fun `deve rejeitar senha apenas com espacos`() {
        assertFalse(clienteTeste.validarSenhaForte("            "))
    }
}
