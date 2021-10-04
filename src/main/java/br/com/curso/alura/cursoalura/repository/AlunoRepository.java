package br.com.curso.alura.cursoalura.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.curso.alura.cursoalura.codec.AlunoCodec;
import br.com.curso.alura.cursoalura.model.Aluno;

@Repository
public class AlunoRepository {

	private MongoClient cliente;
	private MongoDatabase bancoDeDados;

	private void criarConexao() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		AlunoCodec alunoCodec = new AlunoCodec(codec);

		CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(alunoCodec));

		MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

		this.cliente = new MongoClient(new ServerAddress("localhost"), options);
		this.bancoDeDados = cliente.getDatabase("teste");
	}

	private MongoCollection<Aluno> pegaAlunoDoMongoDB() {
		return this.bancoDeDados.getCollection("alunos", Aluno.class);
	}

	private List<Aluno> popularAlunos(MongoCursor<Aluno> mongoCursor) {
		List<Aluno> alunos = new ArrayList<Aluno>();
		while (mongoCursor.hasNext()) {
			Aluno aluno = mongoCursor.next();
			alunos.add(aluno);
		}

		return alunos;
	}

	public void salvar(Aluno aluno) {

		criarConexao();

		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB();

		if (aluno.getId() == null) {
			aluno = aluno.criarID();
			alunosMongo.insertOne(aluno);
		} else {
			alunosMongo.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
		}

		this.cliente.close();
	}

	public List<Aluno> obterTodosAlunos() {
		criarConexao();

		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB();
		MongoCursor<Aluno> mongoCursor = alunosMongo.find().iterator();
		List<Aluno> alunos = popularAlunos(mongoCursor);

		this.cliente.close();
		return alunos;
	}

	public Aluno findById(String id) {
		criarConexao();
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB();
		Aluno aluno = alunosMongo.find(Filters.eq("_id", new ObjectId(id))).first();
		return aluno;
	}

	public List<Aluno> pesquisaPor(String nome) {
		criarConexao();
		MongoCollection<Aluno> alunosMongo = pegaAlunoDoMongoDB();
		MongoCursor<Aluno> mongoCursor = alunosMongo.find(Filters.eq("nome", nome), Aluno.class).iterator();

		List<Aluno> alunos = popularAlunos(mongoCursor);
		this.cliente.close();

		return alunos;
	}

	public List<Aluno> pesquisaPor(String classificacao, Double notacorte) {
		criarConexao();
		MongoCollection<Aluno> alunoDoMongoDB = pegaAlunoDoMongoDB();
		List<Aluno> alunos = null;
		if (classificacao.equals("reprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.lt("nota", notacorte)).iterator();
			alunos = popularAlunos(mongoCursor);
		} else if (classificacao.equals("aprovados")) {
			MongoCursor<Aluno> mongoCursor = alunoDoMongoDB.find(Filters.gte("nota", notacorte)).iterator();
			alunos = popularAlunos(mongoCursor);
		}
		this.cliente.close();
		return alunos;
	}

}
