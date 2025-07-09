package net.originmobi.pdv.service;

import net.originmobi.pdv.enumerado.VendaSituacao;
import net.originmobi.pdv.model.*;
import net.originmobi.pdv.repository.VendaRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class VendaServiceTest {

    @InjectMocks
    private VendaService vendaService;

    @Mock
    private VendaRepository vendas;

    @Mock
    private UsuarioService usuarios;

    @Mock
    private VendaProdutoService vendaProdutos;

    @Mock
    private ProdutoService produtos;

    private Venda venda;
    private Usuario usuario;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup usuario
        usuario = new Usuario();
        usuario.setCodigo(1L);

        // Setup venda
        venda = new Venda();
        venda.setCodigo(1L);
        venda.setUsuario(usuario);
        venda.setSituacao(VendaSituacao.ABERTA);
    }

    @Test
    public void testAbreVenda_VendaNova() {
        // Arrange
        venda.setCodigo(null);
        when(vendas.save(any(Venda.class))).thenReturn(venda);

        // Act
        Long result = vendaService.abreVenda(venda);

        // Assert
        verify(vendas).save(venda);
    }

    @Test
    public void testAbreVenda_VendaExistente() {
        // Arrange
        venda.setCodigo(1L);
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(1L);
        venda.setPessoa(pessoa);
        venda.setObservacao("Observacao teste");

        // Act
        vendaService.abreVenda(venda);

        // Assert
        verify(vendas).updateDadosVenda(any(Pessoa.class), eq("Observacao teste"), eq(1L));
    }

    @Test
    public void testLista() {
        // Arrange
        List<Venda> vendasList = Arrays.asList(venda);
        when(vendas.findAll()).thenReturn(vendasList);

        // Act
        List<Venda> result = vendaService.lista();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(vendas).findAll();
    }

    @Test(expected = RuntimeException.class)
    public void testFechaVenda_VendaJaFechada() {
        // Arrange
        when(vendas.findByCodigoEquals(1L)).thenReturn(venda);
        venda.setSituacao(VendaSituacao.FECHADA);

        // Act
        vendaService.fechaVenda(1L, 1L, 100.0, 0.0, 0.0, new String[]{}, new String[]{});

        // Assert
        // Deve lançar RuntimeException com mensagem "venda fechada"
    }

    @Test
    public void testAddProduto_ComProdutoValido() {
        // Arrange
        Long codVenda = 1L;
        Long codProduto = 1L;
        Double vlBalanca = 0.0;

        Produto produto = new Produto();
        produto.setCodigo(codProduto);
        produto.setDescricao("Produto Teste");

        when(vendas.verificaSituacao(codVenda)).thenReturn(VendaSituacao.ABERTA.toString());
        when(produtos.busca(codProduto)).thenReturn(produto);

        // Act
        String result = vendaService.addProduto(codVenda, codProduto, vlBalanca);

        // Assert
        assertEquals("ok", result);
        verify(vendas).verificaSituacao(codVenda);
    }

    @Test
    public void testRemoveProduto() {
        // Arrange
        Long posicaoProd = 1L;
        Long codVenda = 1L;

        when(vendas.findByCodigoEquals(codVenda)).thenReturn(venda);
        venda.setSituacao(VendaSituacao.ABERTA);

        // Act
        String result = vendaService.removeProduto(posicaoProd, codVenda);

        // Assert
        assertEquals("ok", result);
        verify(vendas).findByCodigoEquals(codVenda);
        verify(vendaProdutos).removeProduto(posicaoProd);
    }
}
