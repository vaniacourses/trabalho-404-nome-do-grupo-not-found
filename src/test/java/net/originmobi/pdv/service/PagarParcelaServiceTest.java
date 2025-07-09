package net.originmobi.pdv.service;

import net.originmobi.pdv.filter.PagarParcelaFilter;
import net.originmobi.pdv.model.Pagar;
import net.originmobi.pdv.model.PagarParcela;
import net.originmobi.pdv.repository.PagarParcelaRespository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PagarParcelaServiceTest {

    @Mock
    private PagarParcelaRespository parcelas;

    @InjectMocks
    private PagarParcelaService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cadastrar_ComValoresValidos_ChamaRepositorio() {
        Double vltotal = 100.0;
        Double vlrestante = 100.0;
        int quitado = 0;
        Timestamp cadastro = new Timestamp(System.currentTimeMillis());
        LocalDate vencimento = LocalDate.now().plusDays(10);
        Pagar pagar = new Pagar();

        service.cadastrar(vltotal, vlrestante, quitado, cadastro, vencimento, pagar);

        verify(parcelas, times(1)).geraParcela(eq(vltotal), eq(vlrestante), eq(0.0), eq(0.0), eq(0.0), eq(quitado), eq(cadastro), eq(vencimento), eq(pagar));
    }

    @Test
    public void cadastrar_ComValorTotalZero_NaoChamaRepositorio() {
        Double vltotal = 0.0;
        Double vlrestante = 100.0;
        int quitado = 0;
        Timestamp cadastro = new Timestamp(System.currentTimeMillis());
        LocalDate vencimento = LocalDate.now().plusDays(10);
        Pagar pagar = new Pagar();

        service.cadastrar(vltotal, vlrestante, quitado, cadastro, vencimento, pagar);

        verify(parcelas, times(1)).geraParcela(eq(vltotal), eq(vlrestante), eq(0.0), eq(0.0), eq(0.0), eq(quitado), eq(cadastro), eq(vencimento), eq(pagar));
    }

    @Test
    public void cadastrar_ComValorTotalNegativo_NaoChamaRepositorio() {
        Double vltotal = -10.0;
        Double vlrestante = 100.0;
        int quitado = 0;
        Timestamp cadastro = new Timestamp(System.currentTimeMillis());
        LocalDate vencimento = LocalDate.now().plusDays(10);
        Pagar pagar = new Pagar();

        service.cadastrar(vltotal, vlrestante, quitado, cadastro, vencimento, pagar);

        verify(parcelas, times(1)).geraParcela(eq(vltotal), eq(vlrestante), eq(0.0), eq(0.0), eq(0.0), eq(quitado), eq(cadastro), eq(vencimento), eq(pagar));
    }

    @Test
    public void cadastrar_RepositorioLancaExcecao_LancaRuntimeException() {
        Double vltotal = 100.0;
        Double vlrestante = 100.0;
        int quitado = 0;
        Timestamp cadastro = new Timestamp(System.currentTimeMillis());
        LocalDate vencimento = LocalDate.now().plusDays(10);
        Pagar pagar = new Pagar();

        doThrow(new RuntimeException("Erro")).when(parcelas).geraParcela(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt(), any(Timestamp.class), any(LocalDate.class), any(Pagar.class));

        try {
            service.cadastrar(vltotal, vlrestante, quitado, cadastro, vencimento, pagar);
            fail("Deveria lançar RuntimeException");
        } catch (RuntimeException e) {
            
        }
        verify(parcelas, times(1)).geraParcela(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt(), any(Timestamp.class), any(LocalDate.class), any(Pagar.class));
    }

    @Test
    public void lista_SemFiltroNome_ChamaListaOrdenadaSemNome() {
        PagarParcelaFilter filter = new PagarParcelaFilter();
        filter.setNome(null);
        Pageable pageable = mock(Pageable.class);
        Page<PagarParcela> page = new PageImpl<>(Collections.<PagarParcela>emptyList());
        when(parcelas.listaOrdenada(pageable)).thenReturn(page);

        Page<PagarParcela> result = service.lista(filter, pageable);

        verify(parcelas, times(1)).listaOrdenada(pageable);
        assertEquals(page, result);
    }

    @Test
    public void lista_ComFiltroNome_ChamaListaOrdenadaComNome() {
        PagarParcelaFilter filter = new PagarParcelaFilter();
        filter.setNome("Fornecedor X");
        Pageable pageable = mock(Pageable.class);
        Page<PagarParcela> page = new PageImpl<>(Collections.<PagarParcela>emptyList());
        when(parcelas.listaOrdenada("Fornecedor X", pageable)).thenReturn(page);

        Page<PagarParcela> result = service.lista(filter, pageable);

        verify(parcelas, times(1)).listaOrdenada("Fornecedor X", pageable);
        assertEquals(page, result);
    }

    @Test
    public void lista_RepositorioRetornaPaginaVazia_RetornaPaginaVazia() {
        PagarParcelaFilter filter = new PagarParcelaFilter();
        filter.setNome("");
        Pageable pageable = mock(Pageable.class);
        Page<PagarParcela> emptyPage = new PageImpl<>(Collections.<PagarParcela>emptyList());
        when(parcelas.listaOrdenada(pageable)).thenReturn(emptyPage);

        Page<PagarParcela> result = service.lista(filter, pageable);

        assertTrue(result.getContent().isEmpty());
    }
}