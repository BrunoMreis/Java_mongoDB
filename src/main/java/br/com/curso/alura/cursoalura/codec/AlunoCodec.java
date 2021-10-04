package br.com.curso.alura.cursoalura.codec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Contato;
import br.com.curso.alura.cursoalura.model.Curso;
import br.com.curso.alura.cursoalura.model.Habilidade;
import br.com.curso.alura.cursoalura.model.Nota;

public class AlunoCodec implements CollectibleCodec<Aluno> {

	private Codec<Document> codec;

	public AlunoCodec(Codec<Document> codec) {
		this.codec = codec;
	}

	@Override
	public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoderContext) {
		Document document = new Document();

		ObjectId id = aluno.getId();
		String nome = aluno.getNome();
		Date dataNascimento = aluno.getDataNascimento();
		Curso curso = aluno.getCurso();
		List<Habilidade> habilidades = aluno.getHabilidades();
		List<Nota> notas = aluno.getNotas();
		Contato contato = aluno.getContato();

		document.put("_id", id);
		document.put("nome", nome);
		document.put("dataNascimento", dataNascimento);
		document.put("curso", new Document("nome", curso.getNome()));

		if (habilidades != null) {
			List<Document> habilidadesDocument = new ArrayList<Document>();
			for (Habilidade habilidade : habilidades) {
				habilidadesDocument
						.add(new Document("nome", habilidade.getNome()).append("nivel", habilidade.getNivel()));
			}
			document.put("habilidades", habilidadesDocument);
		}

		if (notas != null) {
			List<Double> notasParaSalvar = new ArrayList<>();
			for (Nota nota : notas) {
				System.out.println(nota.getNota()+"///////////////////////////////////////////////////////////////////////////////////////");
				notasParaSalvar.add(nota.getNota());
			}
			document.put("notas", notasParaSalvar);
		}

		if (contato != null) {
			var coordinates = new ArrayList<Double>();
			for (Double coordinat : contato.getCordinates()) {
				coordinates.add(coordinat);
			}

			document.put("contato",
					new Document().append("endereco", contato.getEndereco()).append("coordinates", coordinates)
							.append("telefone", contato.getTelefone()).append("E-mail", contato.getEmail()));
		}
		codec.encode(writer, document, encoderContext);
	}

	@Override
	public Class<Aluno> getEncoderClass() {

		return Aluno.class;
	}

	@Override
	public Aluno decode(BsonReader reader, DecoderContext decoderContext) {
		Document document = codec.decode(reader, decoderContext);

		Aluno aluno = new Aluno();

		aluno.setId(document.getObjectId("_id"));
		aluno.setNome(document.getString("nome"));
		aluno.setDataNascimento(document.getDate("dataNascimento"));

		Document cursoDocument = (Document) document.get("curso");

		if (cursoDocument != null) {
			String nomeCurso = (String) cursoDocument.get("nome");
			Curso curso = new Curso();
			curso.setNome(nomeCurso);
			aluno.setCurso(curso);
		}

		List<Document> habilidadesDocument = (List<Document>) document.get("habilidades");

		if (habilidadesDocument != null) {
			List<Habilidade> habilidades = new ArrayList<Habilidade>();

			for (Document habilidade : habilidadesDocument) {

				habilidades.add(new Habilidade(habilidade.getString("nome"), habilidade.getString("nivel"))

				);
				aluno.setHabilidades(habilidades);
			}

		}
		List<Nota> notas = aluno.getNotas();

		if(notas != null) {
		  List<Double> notasParaSalvar = new ArrayList<>();
		  for (Nota nota : notas) {
		    notasParaSalvar.add(nota.getNota()); 
		  }
		  document.put("notas", notasParaSalvar);
		} 

		Document contato = (Document) document.get("contato");
		if (contato != null) {
			String endereco = contato.getString("endereco");
			List<Double> coordinates = (List<Double>) contato.get("coordinates");
			aluno.setContato(new Contato(endereco, coordinates));

		}

		return aluno;
	}

	@Override
	public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
		return documentHasId(aluno) ? aluno.criarID() : aluno;
	}

	@Override
	public boolean documentHasId(Aluno aluno) {
		return aluno.getId() != null;
	}

	@Override
	public BsonValue getDocumentId(Aluno aluno) {
		if (!documentHasId(aluno)) {
			throw new IllegalStateException("O aluno não tem um _id");
		}

		return new BsonString(aluno.getId().toHexString());
	}

}
