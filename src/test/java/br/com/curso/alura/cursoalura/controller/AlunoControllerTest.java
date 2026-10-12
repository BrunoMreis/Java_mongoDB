package br.com.curso.alura.cursoalura.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@ExtendWith(MockitoExtension.class)
class AlunoControllerTest {

	@Mock
	private AlunoRepository alunoRepository;

	@InjectMocks
	private AlunoController controller;

	@Test
	void cadastrarAdicionaNovoAlunoAoModelo() {
		ExtendedModelMap model = new ExtendedModelMap();

		String view = controller.cadastar(model);

		assertThat(view).isEqualTo("aluno/cadastrar");
		assertThat(model.getAttribute("aluno")).isInstanceOf(Aluno.class);
	}

	@Test
	void salvarPersisteAlunoERedirecionaParaInicio() {
		Aluno aluno = new Aluno();

		String view = controller.salvar(aluno);

		verify(alunoRepository).salvar(aluno);
		assertThat(view).isEqualTo("redirect:/");
	}

	@Test
	void listarAdicionaAlunosRetornadosPeloRepositorioAoModelo() {
		List<Aluno> alunos = List.of(new Aluno(), new Aluno());
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.obterTodosAlunos()).thenReturn(alunos);

		String view = controller.listar(model);

		assertThat(view).isEqualTo("aluno/listar");
		assertThat(model.getAttribute("alunos")).isSameAs(alunos);
	}

	@Test
	void visualizarBuscaAlunoEAdicionaAoModelo() {
		String id = "aluno-id";
		Aluno aluno = new Aluno();
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.findById(id)).thenReturn(aluno);

		String view = controller.visualizar(id, model);

		assertThat(view).isEqualTo("aluno/visualizar");
		assertThat(model.getAttribute("aluno")).isSameAs(aluno);
		verify(alunoRepository).findById(id);
	}

	@Test
	void pesquisarNomeRetornaViewDePesquisa() {
		assertThat(controller.pesquisarNome()).isEqualTo("aluno/pesquisarnome");
	}

	@Test
	void pesquisarAdicionaResultadosDoRepositorioAoModelo() {
		String nome = "Ana";
		List<Aluno> alunos = List.of(new Aluno());
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.pesquisaPor(nome)).thenReturn(alunos);

		String view = controller.pesquisar(nome, model);

		assertThat(view).isEqualTo("aluno/pesquisarnome");
		assertThat(model.getAttribute("alunos")).isSameAs(alunos);
		verify(alunoRepository).pesquisaPor(nome);
	}
}
