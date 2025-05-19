package net.originmobi.pdv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManagementTests {

	// Classe a ser testada: UsuarioService

	@Test
	public void cadastrarUsuario() {
		/*
		* Cadastra o usuário, caso o mesmo já não existe e a pessoa não esteja
		* vinculado a outro usuário.
		*/
	}

	@Test
	public void atualizarUsuario() {
		/*
		* Atualiza o usuário
		*/
	}

	@Test
	public void adicionarGrupoSucesso() {
		/*
		* Vincula um grupo ao usuário que não está no grupo
		*/
	}
	
	@Test
	public void adicionarGrupoErro() {
		/*
		* Vincula um grupo ao usuário que já está no grupo
		*/
	}

	@Test
	public void removerGrupo() {
		/*
		* Remove vínculo de um grupo ao usuário
		*/
	}
}
