package br.com.curso.alura.cursoalura.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Date;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Contato;
import br.com.curso.alura.cursoalura.model.Curso;
import br.com.curso.alura.cursoalura.model.Habilidade;
import br.com.curso.alura.cursoalura.model.Nota;

class AlunoCodecTest {

	private final Codec<Document> documentCodec = new DocumentCodec();
	private final AlunoCodec codec = new AlunoCodec(documentCodec);

	@Test
	void encodeSerializaDadosDoAlunoNoDocumentoBson() {
		ObjectId id = new ObjectId();
		Date dataNascimento = new Date();
		Aluno aluno = alunoCompleto(id, dataNascimento);
		BsonDocument bsonDocument = new BsonDocument();

		codec.encode(new BsonDocumentWriter(bsonDocument), aluno, EncoderContext.builder().build());

		Document document = decodeDocument(bsonDocument);
		assertThat(document.getObjectId("_id")).isEqualTo(id);
		assertThat(document.getString("nome")).isEqualTo("Ana");
		assertThat(document.getDate("dataNascimento")).isEqualTo(dataNascimento);
		assertThat(document.get("curso", Document.class).getString("nome")).isEqualTo("Engenharia");
		assertThat(document.getList("habilidades", Document.class))
				.extracting(habilidade -> habilidade.getString("nome"), habilidade -> habilidade.getString("nivel"))
				.containsExactly(org.assertj.core.groups.Tuple.tuple("Java", "Avancado"));
		assertThat(document.getList("notas", Double.class)).containsExactly(9.5, 8.0);

		Document contato = document.get("contato", Document.class);
		assertThat(contato.getString("endereco")).isEqualTo("Rua A");
		assertThat(contato.getList("coordinates", Double.class)).containsExactly(-23.5, -46.6);
		assertThat(contato.getString("telefone")).isEqualTo("11999990000");
		assertThat(contato.getString("E-mail")).isEqualTo("ana@example.com");
	}

	@Test
	void decodeReconstruiAlunoComDadosAninhados() {
		ObjectId id = new ObjectId();
		Date dataNascimento = new Date();
		BsonDocument bsonDocument = encodeDocument(documentAluno(id, dataNascimento));

		Aluno aluno = codec.decode(new BsonDocumentReader(bsonDocument), DecoderContext.builder().build());

		assertThat(aluno.getId()).isEqualTo(id);
		assertThat(aluno.getNome()).isEqualTo("Ana");
		assertThat(aluno.getDataNascimento()).isEqualTo(dataNascimento);
		assertThat(aluno.getCurso().getNome()).isEqualTo("Engenharia");
		assertThat(aluno.getHabilidades()).usingRecursiveFieldByFieldElementComparator()
				.containsExactly(new Habilidade("Java", "Avancado"));
		assertThat(aluno.getNotas()).extracting(Nota::getNota).containsExactly(9.5, 8.0);
		assertThat(aluno.getContato().getEndereco()).isEqualTo("Rua A");
		assertThat(aluno.getContato().getCoordinates()).containsExactly(-23.5, -46.6);
		assertThat(aluno.getContato().getTelefone()).isEqualTo("11999990000");
		assertThat(aluno.getContato().getEmail()).isEqualTo("ana@example.com");
	}

	@Test
	void decodeAlunoSemDadosOpcionaisMantemCamposAninhadosNulos() {
		Document document = new Document("_id", new ObjectId())
				.append("nome", "Ana")
				.append("dataNascimento", new Date());

		Aluno aluno = codec.decode(new BsonDocumentReader(encodeDocument(document)), DecoderContext.builder().build());

		assertThat(aluno.getCurso()).isNull();
		assertThat(aluno.getHabilidades()).isEmpty();
		assertThat(aluno.getNotas()).isEmpty();
		assertThat(aluno.getContato()).isNull();
	}

	@Test
	void getEncoderClassRetornaClasseAluno() {
		assertThat(codec.getEncoderClass()).isEqualTo(Aluno.class);
	}

	@Test
	void documentHasIdIndicaSeAlunoPossuiId() {
		Aluno alunoSemId = new Aluno();
		Aluno alunoComId = new Aluno();
		alunoComId.setId(new ObjectId());

		assertThat(codec.documentHasId(alunoSemId)).isFalse();
		assertThat(codec.documentHasId(alunoComId)).isTrue();
	}

	@Test
	void generateIdIfAbsentFromDocumentCriaIdQuandoAlunoAindaNaoPossuiId() {
		Aluno aluno = new Aluno();

		Aluno resultado = codec.generateIdIfAbsentFromDocument(aluno);

		assertThat(resultado).isSameAs(aluno);
		assertThat(aluno.getId()).isNotNull();
	}

	@Test
	void generateIdIfAbsentFromDocumentPreservaIdExistente() {
		ObjectId id = new ObjectId();
		Aluno aluno = new Aluno();
		aluno.setId(id);

		Aluno resultado = codec.generateIdIfAbsentFromDocument(aluno);

		assertThat(resultado).isSameAs(aluno);
		assertThat(aluno.getId()).isEqualTo(id);
	}

	@Test
	void getDocumentIdRetornaIdBsonDoAluno() {
		ObjectId id = new ObjectId();
		Aluno aluno = new Aluno();
		aluno.setId(id);

		assertThat(codec.getDocumentId(aluno).asString().getValue()).isEqualTo(id.toHexString());
	}

	@Test
	void getDocumentIdSemIdLancaExcecao() {
		assertThatThrownBy(() -> codec.getDocumentId(new Aluno()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("O aluno não tem um _id");
	}

	private Aluno alunoCompleto(ObjectId id, Date dataNascimento) {
		Aluno aluno = new Aluno();
		aluno.setId(id);
		aluno.setNome("Ana");
		aluno.setDataNascimento(dataNascimento);

		Curso curso = new Curso();
		curso.setNome("Engenharia");
		aluno.setCurso(curso);
		aluno.setHabilidades(List.of(new Habilidade("Java", "Avancado")));
		aluno.setNotas(List.of(new Nota(9.5), new Nota(8.0)));

		Contato contato = new Contato("Rua A", List.of(-23.5, -46.6));
		contato.setTelefone("11999990000");
		contato.setEmail("ana@example.com");
		aluno.setContato(contato);
		return aluno;
	}

	private Document documentAluno(ObjectId id, Date dataNascimento) {
		return new Document("_id", id)
				.append("nome", "Ana")
				.append("dataNascimento", dataNascimento)
				.append("curso", new Document("nome", "Engenharia"))
				.append("habilidades", List.of(new Document("nome", "Java").append("nivel", "Avancado")))
				.append("notas", List.of(9.5, 8.0))
				.append("contato", new Document("endereco", "Rua A")
						.append("coordinates", List.of(-23.5, -46.6))
						.append("telefone", "11999990000")
						.append("E-mail", "ana@example.com"));
	}

	private BsonDocument encodeDocument(Document document) {
		BsonDocument bsonDocument = new BsonDocument();
		documentCodec.encode(new BsonDocumentWriter(bsonDocument), document, EncoderContext.builder().build());
		return bsonDocument;
	}

	private Document decodeDocument(BsonDocument bsonDocument) {
		return documentCodec.decode(new BsonDocumentReader(bsonDocument), DecoderContext.builder().build());
	}
}
