package net.originmobi.pdv.integration;

import net.originmobi.pdv.enumerado.VendaSituacao;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.model.Venda;
import net.originmobi.pdv.repository.UsuarioRepository;
import net.originmobi.pdv.repository.VendaRepository;
import net.originmobi.pdv.service.VendaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class VendaServiceIntegrationTest {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testAbreVenda_IntegracaoCompleta() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setSenha("123456");
        usuario = usuarioRepository.save(usuario);

        Venda venda = new Venda();
        venda.setUsuario(usuario);
        venda.setObservacao("Teste de integração");

        Long vendaId = vendaService.abreVenda(venda);

        assertNotNull(vendaId);
        
        Venda vendaSalva = vendaRepository.findById(vendaId).orElse(null);
        assertNotNull(vendaSalva);
        assertEquals(VendaSituacao.ABERTA, vendaSalva.getSituacao());
        assertEquals("Teste de integração", vendaSalva.getObservacao());
        assertEquals(usuario.getCodigo(), vendaSalva.getUsuario().getCodigo());
        assertNotNull(vendaSalva.getData_cadastro());
    }

    @Test
    public void testListaVendas_IntegracaoCompleta() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser2");
        usuario.setSenha("123456");
        usuario = usuarioRepository.save(usuario);

        Venda venda1 = new Venda();
        venda1.setUsuario(usuario);
        venda1.setObservacao("Venda 1");
        venda1.setSituacao(VendaSituacao.ABERTA);
        venda1.setData_cadastro(new Timestamp(System.currentTimeMillis()));
        vendaRepository.save(venda1);

        Venda venda2 = new Venda();
        venda2.setUsuario(usuario);
        venda2.setObservacao("Venda 2");
        venda2.setSituacao(VendaSituacao.FECHADA);
        venda2.setData_cadastro(new Timestamp(System.currentTimeMillis()));
        vendaRepository.save(venda2);

        int totalVendas = vendaService.lista().size();

        assertTrue(totalVendas >= 2);
    }
}
