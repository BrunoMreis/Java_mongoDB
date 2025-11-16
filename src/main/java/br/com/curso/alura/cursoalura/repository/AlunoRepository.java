package br.com.curso.alura.cursoalura.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.curso.alura.cursoalura.codec.AlunoCodec;
import br.com.curso.alura.cursoalura.model.Aluno;

@Repository
public class AlunoRepository {




	private MongoClient criarCliente() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				com.mongodb.MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(new AlunoCodec(com.mongodb.MongoClientSettings.getDefaultCodecRegistry().get(Document.class)))
		);
		com.mongodb.MongoClientSettings settings = com.mongodb.MongoClientSettings.builder()
				.applyToClusterSettings(builder -> builder.hosts(java.util.Collections.singletonList(new com.mongodb.ServerAddress("localhost"))))
				.codecRegistry(pojoCodecRegistry)
				.build();
		return MongoClients.create(settings);
	}

	private MongoDatabase criarBancoDeDados(MongoClient cliente) {
		return cliente.getDatabase("teste");
	}


	private MongoCollection<Aluno> pegaAlunoDoMongoDB(MongoDatabase bancoDeDados) {
		return bancoDeDados.getCollection("alunos", Aluno.class);
	}

	private List<Aluno> popularAlunos(MongoCursor<Aluno> mongoCursor) {
		List<Aluno> alunos = new ArrayList<>();
		while (mongoCursor.hasNext()) {
			Aluno aluno = mongoCursor.next();
			alunos.add(aluno);
		}

		return alunos;
	}

	public void salvar(Aluno aluno) {
		MongoClient cliente = criarCliente();
		MongoDatabase bancoDeDados = criarBancoDeDados(cliente);
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(bancoDeDados);
		if (aluno.getId() == null) {
			aluno = aluno.criarID();
			alunosMongo.insertOne(aluno);
		} else {
			alunosMongo.replaceOne(Filters.eq("_id", aluno.getId()), aluno);
		}
		cliente.close();
	}

	public List<Aluno> obterTodosAlunos() {
		MongoClient cliente = criarCliente();
		MongoDatabase bancoDeDados = criarBancoDeDados(cliente);
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(bancoDeDados);
		MongoCursor<Aluno> mongoCursor = alunosMongo.find().iterator();
		List<Aluno> alunos = popularAlunos(mongoCursor);
		cliente.close();
		return alunos;
	}

	public Aluno findById(String id) {
		MongoClient cliente = criarCliente();
		MongoDatabase bancoDeDados = criarBancoDeDados(cliente);
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(bancoDeDados);
		Aluno aluno = alunosMongo.find(Filters.eq("_id", new ObjectId(id))).first();
		cliente.close();
		return aluno;
	}

	public List<Aluno> pesquisaPor(String nome) {
		MongoClient cliente = criarCliente();
		MongoDatabase bancoDeDados = criarBancoDeDados(cliente);
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB(bancoDeDados);
		MongoCursor<Aluno> mongoCursor = alunosMongo.find(Filters.eq("nome", nome)).iterator();
		List<Aluno> alunos = popularAlunos(mongoCursor);
		cliente.close();
		return alunos;
	}

	public List<Aluno> pesquisaPor(String classificacao, Double notacorte) {
		MongoClient cliente = criarCliente();
		MongoDatabase bancoDeDados = criarBancoDeDados(cliente);
		MongoCollection<Aluno> alunoDoMongoDB = pegaAlunoDoMongoDB(bancoDeDados);
		List<Aluno> alunos = null;
		if (classificacao.equals("reprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.lt("nota", notacorte)).iterator();
			alunos = popularAlunos(mongoCursor);
		} else if (classificacao.equals("aprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.gte("nota", notacorte)).iterator();
			alunos = popularAlunos(mongoCursor);
		}
		cliente.close();
		return alunos;
	}

}
