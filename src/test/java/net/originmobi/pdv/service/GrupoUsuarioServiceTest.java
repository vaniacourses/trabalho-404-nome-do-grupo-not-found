package net.originmobi.pdv.service;

import net.originmobi.pdv.model.GrupoUsuario;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.repository.GrupoUsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GrupoUsuarioServiceTest {

    @InjectMocks
    private GrupoUsuarioService grupoUsuarioService;

    @Mock
    private GrupoUsuarioRepository grupousuarios;

    @Mock
    private RedirectAttributes attributes;

    private GrupoUsuario grupoUsuario;
    private Usuario usuario;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Inicializando usuario
        usuario = new Usuario();
        usuario.setCodigo(1L);

        // Inicializando grupo usuario
        grupoUsuario = new GrupoUsuario();
        grupoUsuario.setCodigo(1L);
        grupoUsuario.setNome("Administrador");
        grupoUsuario.setDescricao("Grupo de administradores");
    }

    @Test
    public void testBuscaGrupos() {
        List<GrupoUsuario> grupos = Arrays.asList(grupoUsuario);
        when(grupousuarios.findByUsuarioIn(usuario)).thenReturn(grupos);

        List<GrupoUsuario> result = grupoUsuarioService.buscaGrupos(usuario);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Administrador", result.get(0).getNome());
        verify(grupousuarios).findByUsuarioIn(usuario);
    }

    @Test
    public void testLista() {
        List<GrupoUsuario> grupos = Arrays.asList(grupoUsuario);
        when(grupousuarios.findAll()).thenReturn(grupos);

        List<GrupoUsuario> result = grupoUsuarioService.lista();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(grupousuarios).findAll();
    }

    @Test
    public void testBuscaGrupo() {
        when(grupousuarios.findByCodigoIn(1L)).thenReturn(grupoUsuario);

        GrupoUsuario result = grupoUsuarioService.buscaGrupo(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getCodigo());
        assertEquals("Administrador", result.getNome());
        verify(grupousuarios).findByCodigoIn(1L);
    }

    @Test
    public void testMerge_NovoGrupo() {
        grupoUsuario.setCodigo(null);
        when(grupousuarios.save(any(GrupoUsuario.class))).thenReturn(grupoUsuario);

        grupoUsuarioService.merge(grupoUsuario, attributes);

        verify(grupousuarios).save(grupoUsuario);
        verify(attributes).addFlashAttribute("mensagem", "Grupo adicionado com sucesso");
    }

    @Test
    public void testMerge_GrupoExistente() {
        grupoUsuario.setCodigo(1L);

        grupoUsuarioService.merge(grupoUsuario, attributes);

        verify(grupousuarios).update(
            eq("Administrador"), 
            eq("Grupo de administradores"), 
            eq(1L)
        );
        verify(attributes).addFlashAttribute("mensagem", "Grupo atualizado com sucesso");
    }

    @Test
    public void testRemove_GrupoSemUsuarios() {
        when(grupousuarios.grupoTemUsuaio(1L)).thenReturn(0);
        
        String result = grupoUsuarioService.remove(1L, attributes);
        
        assertEquals("redirect:/grupousuario", result);
        verify(grupousuarios).deleteById(1L);
    }

    @Test
    public void testRemove_GrupoComUsuarios() {
        when(grupousuarios.grupoTemUsuaio(1L)).thenReturn(1);

        String result = grupoUsuarioService.remove(1L, attributes);

        assertEquals("redirect:/grupousuario/1", result);
        verify(grupousuarios, never()).deleteById(anyLong());
        verify(attributes).addFlashAttribute("mensagemErro", "Este grupo possue usuários vinculados a ele, verifique");
    }

    @Test
    public void testAddPermissao_NovaPermissao() {
        Long codGrupo = 1L;
        Long codPermissao = 1L;
        when(grupousuarios.grupoTemPermissao(codGrupo, codPermissao)).thenReturn(0);

        String result = grupoUsuarioService.addPermissao(codGrupo, codPermissao);

        assertEquals("Permissao adicionada com sucesso", result);
        verify(grupousuarios).addPermissao(codGrupo, codPermissao);
    }

    @Test(expected = RuntimeException.class)
    public void testAddPermissao_PermissaoJaExiste() {
        Long codGrupo = 1L;
        Long codPermissao = 1L;
        when(grupousuarios.grupoTemPermissao(codGrupo, codPermissao)).thenReturn(1);

        grupoUsuarioService.addPermissao(codGrupo, codPermissao);
    }

    @Test
    public void testRemovePermissao() {
        Long codigo = 1L;
        Long codGrupo = 1L;

        String result = grupoUsuarioService.removePermissao(codigo, codGrupo);

        assertEquals("Permissão removida com sucesso", result);
        verify(grupousuarios).removePermissao(codigo, codGrupo);
    }
}