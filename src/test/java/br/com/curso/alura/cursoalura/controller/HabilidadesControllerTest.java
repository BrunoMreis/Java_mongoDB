package br.com.curso.alura.cursoalura.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Habilidade;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@ExtendWith(MockitoExtension.class)
class HabilidadesControllerTest {

	@Mock
	private AlunoRepository alunoRepository;

	@InjectMocks
	private HabilidadesController controller;

	@Test
	void cadastrarAdicionaAlunoEHabilidadeVaziaAoModelo() {
		String id = "aluno-id";
		Aluno aluno = new Aluno();
		ExtendedModelMap model = new ExtendedModelMap();
		when(alunoRepository.findById(id)).thenReturn(aluno);

		String view = controller.cadastrar(id, model);

		assertThat(view).isEqualTo("habilidades/cadastrar");
		assertThat(model.getAttribute("aluno")).isSameAs(aluno);
		assertThat(model.getAttribute("habilidade")).isInstanceOf(Habilidade.class);
		verify(alunoRepository).findById(id);
	}

	@Test
	void salvarAdicionaHabilidadePersisteAlunoERedireciona() {
		String id = "aluno-id";
		Aluno aluno = new Aluno();
		Habilidade habilidade = new Habilidade("Java", "Avancado");
		when(alunoRepository.findById(id)).thenReturn(aluno);

		String view = controller.salvar(id, habilidade);

		assertThat(aluno.getHabilidades()).containsExactly(habilidade);
		verify(alunoRepository).salvar(aluno);
		assertThat(view).isEqualTo("redirect:/aluno/listar");
	}
}
