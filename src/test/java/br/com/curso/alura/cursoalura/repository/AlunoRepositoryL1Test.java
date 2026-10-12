package br.com.curso.alura.cursoalura.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Curso;
import br.com.curso.alura.cursoalura.model.Nota;

@Tag("Layer1")
@Testcontainers
@SpringBootTest
class AlunoRepositoryL1Test {

	@Container
	private static final MongoDBContainer MONGO = new MongoDBContainer(DockerImageName.parse("mongo:7.0.14"));

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private MongoClient mongoClient;

	@DynamicPropertySource
	static void configurarMongoUri(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
	}

	@BeforeEach
	void limparColecao() {
		colecao().deleteMany(new Document());
	}

	@Test
	void salvarEConsultarAlunoPreservaCamposECodec() {
		Aluno aluno = criarAluno("Ana", 9.0);

		alunoRepository.salvar(aluno);
		Aluno encontrado = alunoRepository.findById(aluno.getId().toHexString());

		assertThat(encontrado).isNotNull();
		assertThat(encontrado.getId()).isEqualTo(aluno.getId());
		assertThat(encontrado.getNome()).isEqualTo("Ana");
		assertThat(encontrado.getDataNascimento()).isEqualTo(aluno.getDataNascimento());
		assertThat(encontrado.getCurso().getNome()).isEqualTo("Engenharia");
		assertThat(encontrado.getNotas()).extracting(Nota::getNota).containsExactly(9.0);
	}

	@Test
	void pesquisaPorNomeRetornaSomenteAlunosCorrespondentes() {
		alunoRepository.salvar(criarAluno("Ana", 9.0));
		alunoRepository.salvar(criarAluno("Bruno", 6.0));

		List<Aluno> encontrados = alunoRepository.pesquisaPor("Ana");

		assertThat(encontrados).extracting(Aluno::getNome).containsExactly("Ana");
	}

	@Test
	void pesquisaPorClassificacaoConsultaNotasPersistidas() {
		alunoRepository.salvar(criarAluno("Aprovado", 8.0));
		alunoRepository.salvar(criarAluno("Reprovado", 5.0));

		List<Aluno> aprovados = alunoRepository.pesquisaPor("aprovados", 7.0);
		List<Aluno> reprovados = alunoRepository.pesquisaPor("reprovados", 7.0);

		assertThat(aprovados).extracting(Aluno::getNome).containsExactly("Aprovado");
		assertThat(reprovados).extracting(Aluno::getNome).containsExactly("Reprovado");
	}

	private MongoCollection<Aluno> colecao() {
		return mongoClient.getDatabase("teste").getCollection("alunos", Aluno.class);
	}

	private Aluno criarAluno(String nome, double nota) {
		Aluno aluno = new Aluno();
		aluno.setNome(nome);
		aluno.setDataNascimento(new Date(0));

		Curso curso = new Curso();
		curso.setNome("Engenharia");
		aluno.setCurso(curso);
		aluno.setNotas(List.of(new Nota(nota)));
		return aluno;
	}
}
