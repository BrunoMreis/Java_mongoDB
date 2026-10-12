package br.com.curso.alura.cursoalura.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import br.com.curso.alura.cursoalura.model.Nota;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@ExtendWith(MockitoExtension.class)
class NotasControllerTest {

	@Mock
	private AlunoRepository alunoRepository;

	@InjectMocks
	private NotasController controller;

	@Test
	void cadastrarAdicionaAlunoENotaVaziaAoModelo() {
		String id = "aluno-id";
		Aluno aluno = new Aluno();
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.findById(id)).thenReturn(aluno);

		String view = controller.cadastrar(id, model);

		assertThat(view).isEqualTo("/nota/cadastrar");
		assertThat(model.getAttribute("aluno")).isSameAs(aluno);
		assertThat(model.getAttribute("nota")).isInstanceOf(Nota.class);
		verify(alunoRepository).findById(id);
	}

	@Test
	void salvarAdicionaNotaPersisteAlunoERedireciona() {
		String id = "aluno-id";
		Aluno aluno = new Aluno();
		Nota nota = new Nota(9.5);
		when(alunoRepository.findById(id)).thenReturn(aluno);

		String view = controller.cadastar(id, nota);

		assertThat(aluno.getNotas()).containsExactly(nota);
		verify(alunoRepository).salvar(aluno);
		assertThat(view).isEqualTo("redirect:/aluno/listar");
	}

	@Test
	void iniciarPesquisarRetornaViewDePesquisa() {
		assertThat(controller.iniciarPesquisar()).isEqualTo("nota/pesquisar");
	}

	@Test
	void pesquisarConverteNotaDeCorteEAdicionaResultadosAoModelo() {
		String classificacao = "aprovados";
		List<Aluno> alunos = List.of(new Aluno());
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.pesquisaPor(classificacao, 7.5)).thenReturn(alunos);

		String view = controller.pesquisar(classificacao, "7.5", model);

		assertThat(view).isEqualTo("nota/pesquisar");
		assertThat(model.getAttribute("alunos")).isSameAs(alunos);
		verify(alunoRepository).pesquisaPor(classificacao, 7.5);
	}

	@Test
	void pesquisarComNotaDeCorteInvalidaPropagaErroDeConversao() {
		ExtendedModelMap model = new ExtendedModelMap();

		assertThatThrownBy(() -> controller.pesquisar("aprovados", "invalida", model))
				.isInstanceOf(NumberFormatException.class);
	}
}
