package br.com.curso.alura.cursoalura.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class AlunoTest {

	@Test
	void getNotasInicializaListaQuandoAindaNaoExiste() {
		Aluno aluno = new Aluno();

		List<Nota> notas = aluno.getNotas();

		assertThat(notas).isEmpty();
		assertThat(aluno.getNotas()).isSameAs(notas);
	}

	@Test
	void getHabilidadesInicializaListaQuandoAindaNaoExiste() {
		Aluno aluno = new Aluno();

		List<Habilidade> habilidades = aluno.getHabilidades();

		assertThat(habilidades).isEmpty();
		assertThat(aluno.getHabilidades()).isSameAs(habilidades);
	}

	@Test
	void criarIDDefineIdentificadorEDevolveMesmoAluno() {
		Aluno aluno = new Aluno();

		Aluno resultado = aluno.criarID();

		assertThat(resultado).isSameAs(aluno);
		assertThat(aluno.getId()).isNotNull();
	}

	@Test
	void adicionaNotaAdicionaNotaAListaExistente() {
		Aluno aluno = new Aluno();
		Nota notaExistente = new Nota(7.0);
		Nota novaNota = new Nota(9.5);
		aluno.setNotas(new ArrayList<>(List.of(notaExistente)));

		Aluno resultado = aluno.adicionaNota(aluno, novaNota);

		assertThat(resultado).isSameAs(aluno);
		assertThat(aluno.getNotas()).containsExactly(notaExistente, novaNota);
	}

	@Test
	void adicionaNotaInicializaListaQuandoAindaNaoExiste() {
		Aluno aluno = new Aluno();
		Nota nota = new Nota(9.5);

		aluno.adicionaNota(aluno, nota);

		assertThat(aluno.getNotas()).containsExactly(nota);
	}

	@Test
	void adicionaHabilidadeAdicionaHabilidadeAListaExistente() {
		Aluno aluno = new Aluno();
		Habilidade habilidadeExistente = new Habilidade("Java", "Intermediario");
		Habilidade novaHabilidade = new Habilidade("MongoDB", "Basico");
		aluno.setHabilidades(new ArrayList<>(List.of(habilidadeExistente)));

		Aluno resultado = aluno.adicionaHabilidade(aluno, novaHabilidade);

		assertThat(resultado).isSameAs(aluno);
		assertThat(aluno.getHabilidades()).containsExactly(habilidadeExistente, novaHabilidade);
	}

	@Test
	void adicionaHabilidadeInicializaListaQuandoAindaNaoExiste() {
		Aluno aluno = new Aluno();
		Habilidade habilidade = new Habilidade("Java", "Avancado");

		aluno.adicionaHabilidade(aluno, habilidade);

		assertThat(aluno.getHabilidades()).containsExactly(habilidade);
	}
}
