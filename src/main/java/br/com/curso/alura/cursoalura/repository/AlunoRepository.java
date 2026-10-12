package br.com.curso.alura.cursoalura.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.curso.alura.cursoalura.model.Aluno;

@Repository
public class AlunoRepository {

	private static final String NOME_BANCO = "teste";
	private final MongoClient cliente;

	public AlunoRepository(MongoClient cliente) {
		this.cliente = cliente;
	}

	private MongoCollection<Aluno> pegaAlunoDoMongoDB(MongoDatabase bancoDeDados) {
		return bancoDeDados.getCollection("alunos", Aluno.class);
	}

	private List<Aluno> popularAlunos(MongoCursor<Aluno> mongoCursor) {
		List<Aluno> alunos = new ArrayList<>();
		try (mongoCursor) {
			while (mongoCursor.hasNext()) {
				alunos.add(mongoCursor.next());
			}
		}

		return alunos;
	}

	public void salvar(Aluno aluno) {
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(cliente.getDatabase(NOME_BANCO));
		if (aluno.getId() == null) {
			aluno = aluno.criarID();
			alunosMongo.insertOne(aluno);
		} else {
			alunosMongo.replaceOne(Filters.eq("_id", aluno.getId()), aluno);
		}
	}

	public List<Aluno> obterTodosAlunos() {
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(cliente.getDatabase(NOME_BANCO));
		MongoCursor<Aluno> mongoCursor = alunosMongo.find().iterator();
		return popularAlunos(mongoCursor);
	}

	public Aluno findById(String id) {
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(cliente.getDatabase(NOME_BANCO));
		return alunosMongo.find(Filters.eq("_id", new ObjectId(id))).first();
	}

	public List<Aluno> pesquisaPor(String nome) {
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(cliente.getDatabase(NOME_BANCO));
		MongoCursor<Aluno> mongoCursor = alunosMongo.find(Filters.eq("nome", nome)).iterator();
		return popularAlunos(mongoCursor);
	}

	public List<Aluno> pesquisaPor(String classificacao, Double notacorte) {
		MongoCollection<Aluno> alunoDoMongoDB = pegaAlunoDoMongoDB(cliente.getDatabase(NOME_BANCO));
		if (classificacao.equals("reprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.lt("notas", notacorte)).iterator();
			return popularAlunos(mongoCursor);
		}
		if (classificacao.equals("aprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.gte("notas", notacorte)).iterator();
			return popularAlunos(mongoCursor);
		}
		return List.of();
	}

}
