package br.com.curso.alura.cursoalura.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.com.curso.alura.cursoalura.model.Aluno;

@ExtendWith(MockitoExtension.class)
class AlunoRepositoryTest {

	@Mock
	private MongoClient mongoClient;
	@Mock
	private MongoDatabase database;
	@Mock
	private MongoCollection<Aluno> collection;
	@Mock
	private FindIterable<Aluno> findIterable;
	@Mock
	private MongoCursor<Aluno> cursor;
	private AlunoRepository repository;

	@BeforeEach
	void configurarMongoMocks() {
		repository = new AlunoRepository(mongoClient);
		when(mongoClient.getDatabase("teste")).thenReturn(database);
		when(database.getCollection("alunos", Aluno.class)).thenReturn(collection);
	}

	@Test
	void salvarAlunoSemIdGeraIdEInsereNovoDocumento() {
		Aluno aluno = new Aluno();

		repository.salvar(aluno);
		assertThat(aluno.getId()).isNotNull();
		verify(collection).insertOne(aluno);
		verify(collection).insertOne(aluno);
	}

	@Test
	void salvarAlunoComIdSubstituiDocumentoExistente() {
		ObjectId id = new ObjectId();
		Aluno aluno = new Aluno();
		aluno.setId(id);

		repository.salvar(aluno);

		ArgumentCaptor<Bson> filtro = ArgumentCaptor.forClass(Bson.class);
		verify(collection).replaceOne(filtro.capture(), org.mockito.ArgumentMatchers.same(aluno));
		assertThat(render(filtro.getValue()).getObjectId("_id").getValue()).isEqualTo(id);
	}

	@Test
	void obterTodosAlunosRetornaDocumentosDoCursor() {
		Aluno primeiro = new Aluno();
		Aluno segundo = new Aluno();
		when(collection.find()).thenReturn(findIterable);
		when(findIterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true, true, false);
		when(cursor.next()).thenReturn(primeiro, segundo);

		List<Aluno> alunos = repository.obterTodosAlunos();

		assertThat(alunos).containsExactly(primeiro, segundo);
		verify(cursor).close();
	}

	@Test
	void findByIdBuscaPeloObjectIdEDevolveAlunoEncontrado() {
		String id = new ObjectId().toHexString();
		Aluno aluno = new Aluno();
		when(collection.find(any(Bson.class))).thenReturn(findIterable);
		when(findIterable.first()).thenReturn(aluno);

		Aluno resultado = repository.findById(id);

		assertThat(resultado).isSameAs(aluno);
		ArgumentCaptor<Bson> filtro = ArgumentCaptor.forClass(Bson.class);
		verify(collection).find(filtro.capture());
		assertThat(render(filtro.getValue()).getObjectId("_id").getValue().toHexString())
				.isEqualTo(id);
	}

	@Test
	void pesquisaPorNomeFiltraPeloNomeERetornaResultados() {
		String nome = "Ana";
		List<Aluno> alunosEsperados = List.of(new Aluno());
		prepararCursor(alunosEsperados);

		List<Aluno> resultado = repository.pesquisaPor(nome);

		assertThat(resultado).isEqualTo(alunosEsperados);
		ArgumentCaptor<Bson> filtro = ArgumentCaptor.forClass(Bson.class);
		verify(collection).find(filtro.capture());
		assertThat(render(filtro.getValue()).getString("nome").getValue()).isEqualTo(nome);
	}

	@Test
	void pesquisaPorClassificacaoReprovadosUsaFiltroMenorQue() {
		prepararCursor(List.of(new Aluno()));

		List<Aluno> resultado = repository.pesquisaPor("reprovados", 7.0);

		assertThat(resultado).hasSize(1);
		ArgumentCaptor<Bson> filtro = ArgumentCaptor.forClass(Bson.class);
		verify(collection).find(filtro.capture());
		assertThat(render(filtro.getValue()).getDocument("notas").getDouble("$lt").getValue()).isEqualTo(7.0);
	}

	@Test
	void pesquisaPorClassificacaoAprovadosUsaFiltroMaiorOuIgual() {
		prepararCursor(List.of());

		List<Aluno> resultado = repository.pesquisaPor("aprovados", 7.0);

		assertThat(resultado).isEmpty();
		ArgumentCaptor<Bson> filtro = ArgumentCaptor.forClass(Bson.class);
		verify(collection).find(filtro.capture());
		assertThat(render(filtro.getValue()).getDocument("notas").getDouble("$gte").getValue()).isEqualTo(7.0);
	}

	@Test
	void pesquisaPorClassificacaoDesconhecidaRetornaNuloEFechaCliente() {
		List<Aluno> resultado = repository.pesquisaPor("indefinida", 7.0);

		assertThat(resultado).isEmpty();
	}

	private void prepararCursor(List<Aluno> alunos) {
		when(collection.find(any(Bson.class))).thenReturn(findIterable);
		when(findIterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(!alunos.isEmpty(), false);
		if (!alunos.isEmpty()) {
			when(cursor.next()).thenReturn(alunos.getFirst());
		}
	}

	private org.bson.BsonDocument render(Bson bson) {
		return bson.toBsonDocument(org.bson.Document.class, MongoClientSettings.getDefaultCodecRegistry());
	}
}
