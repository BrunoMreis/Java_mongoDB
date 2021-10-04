package br.com.curso.alura.cursoalura.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

public class Aluno {

	private ObjectId id;
	private String nome;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataNascimento;
	private Curso curso;
	private List<Nota> notas;
	private List<Habilidade> habilidades;
	private Contato contato;

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {

		this.dataNascimento = dataNascimento;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Nota> getNotas() {
		if(notas == null) {
			notas = new ArrayList<Nota>();
		}
		
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public List<Habilidade> getHabilidades() {
		if (habilidades == null) {
			habilidades = new ArrayList<Habilidade>();
		}

		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Aluno criarID() {
		setId(new ObjectId());
		return this;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + ", dataNascimento=" + dataNascimento + ", curso=" + curso
				+ ", notas=" + notas + ", habilidades=" + habilidades + ", contato=" + contato + "]";
	}

	public Aluno adicionaHabilidade(Aluno aluno, Habilidade habilidade) {
		List<Habilidade> habilidadesAtualizada = aluno.getHabilidades();
		habilidadesAtualizada.add(habilidade);
		aluno.setHabilidades(habilidadesAtualizada);

		return aluno;
	}

	public Aluno adicionaNota(Aluno aluno, Nota nota) {
		List<Nota> notaAtualizada = aluno.getNotas();
		notaAtualizada.add(nota);
		aluno.setNotas(notaAtualizada);
		return aluno;
	}

}
