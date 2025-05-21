package net.originmobi.pdv;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.originmobi.pdv.model.GrupoUsuario;
import net.originmobi.pdv.model.Pessoa;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.repository.UsuarioRepository;
import net.originmobi.pdv.service.GrupoUsuarioService;
import net.originmobi.pdv.service.UsuarioService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserManagementTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GrupoUsuarioService grupoUsuarioService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Pessoa pessoa;
    private GrupoUsuario grupo;

    @Before
    public void setup() {
        pessoa = new Pessoa();
        pessoa.setCodigo(1L);

        usuario = new Usuario();
        usuario.setCodigo(null);
        usuario.setUser("teste");
        usuario.setSenha("123");
        usuario.setPessoa(pessoa);
        usuario.setGrupoUsuario(new ArrayList<>());

        grupo = new GrupoUsuario();
        grupo.setCodigo(1L);
    }

    @Test
    public void cadastrarUsuario() {
        when(usuarioRepository.findByUserEquals("teste")).thenReturn(null);
        when(usuarioRepository.findByPessoaCodigoEquals(1L)).thenReturn(null);

        String resultado = usuarioService.cadastrar(usuario);
        assertEquals("Usuário salvo com sucesso", resultado);
    }

    @Test
    public void atualizarUsuario() {
        usuario.setCodigo(10L);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        String resultado = usuarioService.cadastrar(usuario);
        assertEquals("Usuário atualizado com sucesso", resultado);
    }

    @Test
    public void adicionarGrupoSucesso() {
        when(usuarioRepository.findByCodigoIn(1L)).thenReturn(usuario);
        when(grupoUsuarioService.buscaGrupo(1L)).thenReturn(grupo);

        String resultado = usuarioService.addGrupo(1L, 1L);
        assertEquals("ok", resultado);
    }

    @Test
    public void adicionarGrupoErro() {
        usuario.getGrupoUsuario().add(grupo);

        when(usuarioRepository.findByCodigoIn(1L)).thenReturn(usuario);
        when(grupoUsuarioService.buscaGrupo(1L)).thenReturn(grupo);

        String resultado = usuarioService.addGrupo(1L, 1L);
        assertEquals("ja existe", resultado);
    }

    @Test
    public void removerGrupo() {
        usuario.getGrupoUsuario().add(grupo);

        List<GrupoUsuario> listaGrupos = new ArrayList<>();
        listaGrupos.add(grupo);

        when(usuarioRepository.findByCodigoIn(1L)).thenReturn(usuario);
        when(grupoUsuarioService.buscaGrupo(1L)).thenReturn(grupo);
        when(grupoUsuarioService.buscaGrupos(usuario)).thenReturn(listaGrupos);

        String resultado = usuarioService.removeGrupo(1L, 1L);
        assertEquals("ok", resultado);
    }
}
