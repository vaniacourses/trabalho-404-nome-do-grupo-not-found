package net.originmobi.pdv.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import net.originmobi.pdv.enumerado.caixa.CaixaTipo;
import net.originmobi.pdv.model.Caixa;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.repository.CaixaRepository;
import net.originmobi.pdv.singleton.Aplicacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CaixaServiceTest {

    @Mock
    private CaixaRepository caixaRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CaixaLancamentoService lancamentoService;

    @InjectMocks
    private CaixaService caixaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Simula o singleton para não dar erro no teste
        Aplicacao.getInstancia().setUsuarioAtual("usuarioTeste");
    }

    // Teste 1: Abertura de caixa com valor negativo
    @Test
    public void testCadastroCaixaComValorNegativo() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.CAIXA);
        caixa.setValor_abertura(-100.0);

        when(caixaRepository.caixaAberto()).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caixaService.cadastro(caixa);
        });

        assertEquals("Valor informado é inválido", exception.getMessage());
    }

    // Teste 2: Verificação de caixa aberto
    @Test
    public void testCaixaIsAberto() {
        when(caixaRepository.caixaAberto()).thenReturn(Optional.of(new Caixa()));

        boolean isAberto = caixaService.caixaIsAberto();
        assertTrue(isAberto);
    }

    // Teste 3: Fechamento de caixa com senha incorreta
    @Test
    public void testFechaCaixaSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("$2a$10$7"); // senha: "senha123"

        when(usuarioService.buscaUsuario(anyString())).thenReturn(usuario);

        String resposta = caixaService.fechaCaixa(1L, "senhaErrada");

        assertEquals("Senha incorreta, favor verifique", resposta);
    }

    // Teste 4: Definir descrição padrão para cofre sem descrição
    @Test
    public void testDescricaoPadraoParaCofre() {
        Caixa caixa = new Caixa();
        caixa.setTipo(CaixaTipo.COFRE);
        caixa.setValor_abertura(0.0);
        caixa.setDescricao("");

        when(caixaRepository.caixaAberto()).thenReturn(Optional.empty());
        when(usuarioService.buscaUsuario(anyString())).thenReturn(new Usuario());
        when(caixaRepository.save(any(Caixa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long id = caixaService.cadastro(caixa);

        assertEquals("Cofre", caixa.getDescricao());
    }
}
